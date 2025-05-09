package com.aspodev.parser.Behavior;

import java.util.Arrays;
import java.util.List;

import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.TokenTypes;
import com.aspodev.parser.Instructions.Instruction;
import com.aspodev.parser.Scope.ScopeEnum;

public class OtherBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		List<Token> identifers = instruction.getTokens().stream().filter(t -> {
			if (!t.isIdentifier())
				return false;

			boolean chainIdf = t.getType() == TokenTypes.CHAINED_IDENTIFIER && !t.getValue().startsWith(".");

			if (chainIdf)
				return true;

			Token nextToken = instruction.getToken(t.getPosition() + 1);
			return nextToken != null && nextToken.getValue().equals("(");
		}).toList();
		List<Token> tokens = instruction.getTokens();

		for (Token idf : identifers) {

			/**
			 * This means they are part of a chain call like "instruction.getTokens" ()
			 * ".stream" ()
			 */
			if (idf.getValue().startsWith("."))
				continue;

			Token nextToken = tokens.get(idf.getPosition() + 1);

			if (!nextToken.getValue().equals("("))
				continue;

			if (idf.getType() == TokenTypes.CHAINED_IDENTIFIER) {
				String[] components = idf.getValue().split("\\.");
				String varType = context.getVariableType(components[0]);

				if (context.isType(new Token(components[0]))) {
					varType = components[0];
				}

				if (varType == null) {
					varType = "UNKOWN";
					System.out.println("[DEBUG] Unkown dependency: " + Arrays.asList(components));
				}

				context.addDependency(components[1], varType);
			} else {
				String callerType = "RESOLVE";

				if (context.isType(idf)) {
					callerType = idf.getValue();
				}

				if (instruction.getToken(idf.getPosition() - 1).equals(new Token("new"))) {
					callerType = idf.getValue();
				}

				context.addDependency(idf.getValue(), callerType);
			}
		}

		if (tokens.contains(new Token("{")))
			context.changeScope(ScopeEnum.INSTRUCTION);

	}

}

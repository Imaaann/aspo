package com.aspodev.parser.Behavior;

import java.util.List;

import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.TokenTypes;
import com.aspodev.parser.Instructions.Instruction;
import com.aspodev.parser.Instructions.InstructionUtil;
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

			int finalPos = InstructionUtil.resolveTypeLength(instruction.getTokens(), t.getPosition());
			Token nextToken = instruction.getToken(finalPos + 1);
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

			if (idf.getType() == TokenTypes.CHAINED_IDENTIFIER) {
				String[] components = idf.getValue().split("\\.");
				String varType = context.getVariableType(components[0]);

				if (context.isType(new Token(components[0]))) {
					varType = components[0];
				}

				if (varType == null) {
					varType = "UNKOWN";
				}

				context.addDependency(components[1], varType);
			} else {
				String callerType = "RESOLVE";

				if (context.isType(idf)) {
					callerType = idf.getValue() + ".static";
				}

				if (instruction.getToken(idf.getPosition() - 1).equals(new Token("new"))) {
					callerType = idf.getValue() + ".construct";
				}

				context.addDependency(idf.getValue(), callerType);
			}
		}

		List<Token> methodRefrenceList = tokens.stream().filter(t -> t.getValue().equals("::")).toList();
		for (Token methodReference : methodRefrenceList) {
			Token nextToken = instruction.getToken(methodReference.getPosition() + 1);
			Token prevToken = instruction.getToken(methodReference.getPosition() - 1);

			String typeName;
			if (!prevToken.isIdentifier()) {
				typeName = resolveLiteral(prevToken);
			} else {
				typeName = context.getVariableType(prevToken.getValue());
				if (typeName == null)
					typeName = prevToken.getValue();
			}

			if (nextToken.getValue().equals("new")) {
				context.addDependency(typeName + ".construct", typeName);
			} else {
				context.addDependency(nextToken.getValue(), typeName);
			}

		}

		if (tokens.contains(new Token("{")))
			context.changeScope(ScopeEnum.INSTRUCTION);

	}

	private String resolveLiteral(Token token) {
		String value = token.getValue();
		if (value.equals("null"))
			return "null";
		if (value.equals("true") || value.equals("false"))
			return "boolean";
		if (token.getType() == TokenTypes.LITERAL) {
			if (value.contains(".") || value.contains("p"))
				return "double";
			return "int";
		}

		return "String";
	}

}

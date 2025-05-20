package com.aspodev.parser.Behavior;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.TokenTypes;
import com.aspodev.parser.Instructions.Instruction;
import com.aspodev.parser.Instructions.InstructionUtil;
import com.aspodev.parser.Scope.ScopeEnum;

public class OtherBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		List<Token> identifiers = instruction.getTokens().stream().filter(t -> {
			if (!t.isIdentifier())
				return false;

			if (context.isAttribute(t.getValue()))
				return true;

			String next = instruction.getToken(t.getPosition() + 1).getValue();
			boolean chainIdf = t.getType() == TokenTypes.CHAINED_IDENTIFIER && (next.equals("(") || next.contains("<"));

			if (chainIdf)
				return true;

			int finalPos = InstructionUtil.resolveTypeLength(instruction.getTokens(), t.getPosition());
			Token nextToken = instruction.getToken(finalPos + 1);
			return nextToken != null && nextToken.getValue().equals("(");

		}).toList();

		List<Token> tokens = instruction.getTokens();

		for (Token idf : identifiers) {

			if (context.isAttribute(idf.getValue())) {
				context.addAttributeDependency(idf.getValue());
				continue;
			}

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
					varType = "UNKNOWN";
				}

				context.addDependency(components[1], varType);

				if (context.isAttribute(components[0]))
					context.addAttributeDependency(components[0]);
				else if (components[0].equals("this") && context.isAttribute(components[1]))
					context.addAttributeDependency(components[1]);

			} else {
				String callerType = "RESOLVE";

				if (context.isType(idf)) {
					callerType = idf.getValue();
				}

				if (instruction.getToken(idf.getPosition() - 1).equals(new Token("new"))) {
					callerType = idf.getValue() + ".construct";
				}

				context.addDependency(idf.getValue(), callerType);
			}
		}

		List<Token> methodReferenceList = tokens.stream().filter(t -> t.getValue().equals("::")).toList();
		for (Token methodReference : methodReferenceList) {
			Token nextToken = instruction.getToken(methodReference.getPosition() + 1);
			Token prevToken = instruction.getToken(methodReference.getPosition() - 1);

			String typeName;
			if (!prevToken.isIdentifier()) {
				typeName = InstructionUtil.resolveLiteral(prevToken);
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

		Set<String> calledMethods = identifiers.stream().map(t -> t.getValue()).map(s -> extract(s))
				.collect(Collectors.toSet());
		context.addCalledMethods(calledMethods);

		if (tokens.contains(new Token("{")))
			context.changeScope(ScopeEnum.INSTRUCTION);

	}

	private String extract(String s) {
		if (!s.contains("."))
			return s;
		if (s.startsWith("."))
			return s.replaceFirst("\\.", "");
		return Arrays.stream(s.split("\\.")).reduce((a, b) -> b).orElse("");
	}

}

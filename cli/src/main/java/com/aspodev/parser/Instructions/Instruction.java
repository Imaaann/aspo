package com.aspodev.parser.Instructions;

import java.util.ArrayList;
import java.util.List;

import com.aspodev.SCAR.Modifier;
import com.aspodev.parser.Token;
import com.aspodev.parser.TokenNotFoundException;

public class Instruction {
	private final List<Token> tokens;
	private InstructionTypes type;

	public Instruction(List<Token> tokens) {
		this.tokens = tokens;
	}

	public void setType(InstructionTypes type) {
		this.type = type;
	}

	public InstructionTypes getType() {
		return this.type;
	}

	public Token getIdentifier(int index) throws TokenNotFoundException {
		return Instruction.getIdentifier(tokens, index);
	}

	public List<Modifier> getModifiers() {
		List<Modifier> result = new ArrayList<>();
		boolean normalClassDeclaration = isTypeDeclaration();

		for (Token token : tokens) {
			if (isModifier(token))
				result.add(Modifier.convert(token.getValue()));

			if (normalClassDeclaration) {
				if (token.getValue().equals("sealed"))
					result.add(Modifier.SEALED);

				if (token.getValue().equals("non")) {
					int nonPosition = token.getPosition();
					if (tokens.get(nonPosition + 1).getValue().equals("-")
							&& tokens.get(nonPosition + 2).getValue().equals("sealed"))
						result.add(Modifier.NON_SEALED);
				}
			}
		}

		return result;
	}

	public List<Token> getTokens() {
		return this.tokens;
	}

	public static Token getIdentifier(List<Token> tokens, int index) {
		for (Token token : tokens) {
			if (token.isIdentifier()) {
				if (index == 0)
					return token;
				index--;
			}
		}

		throw new TokenNotFoundException("Identifier number " + index + "not found");
	}

	public String toString() {
		return tokens + " TYPE:" + type;
	}

	private boolean isTypeDeclaration() {
		return type == InstructionTypes.CLASS_DECLARATION || type == InstructionTypes.RECORD_DEFINITION
				|| type == InstructionTypes.ENUM_DECLARTION || type == InstructionTypes.INTERFACE_DECLARATION;
	}

	private boolean isModifier(Token token) {
		String value = token.getValue();

		return value.equals("abstract") || value.equals("synchronized") || value.equals("transient")
				|| value.equals("final") || value.equals("static") || value.equals("strictfp")
				|| value.equals("volatile");
	}
}

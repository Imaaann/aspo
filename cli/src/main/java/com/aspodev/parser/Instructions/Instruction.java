package com.aspodev.parser.Instructions;

import java.util.List;

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
}

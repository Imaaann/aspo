package com.aspodev.parser.Instructions;

import java.util.List;

import com.aspodev.parser.Token;
import com.aspodev.parser.TokenNotFoundException;

public record Instruction(List<Token> tokens, InstructionTypes type) {
	public Token getIdentifier(int index) throws TokenNotFoundException {
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

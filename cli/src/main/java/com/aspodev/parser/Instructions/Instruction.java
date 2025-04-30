package com.aspodev.parser.Instructions;

import java.util.List;

import com.aspodev.parser.Token;

public record Instruction(List<Token> tokens, InstructionTypes type) {
	public String toString() {
		return tokens + " TYPE:" + type;
	}
}

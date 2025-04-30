package com.aspodev.parser.Instructions;

import java.util.ArrayList;
import java.util.List;

import com.aspodev.parser.ParserConstants;
import com.aspodev.parser.Token;
import com.aspodev.parser.TokenTypes;

public class InstructionClassifier {
	private List<String> rawInstruction;
	private List<Token> classifiedTokens;
	private InstructionTypes instructionType;

	public InstructionClassifier(List<String> instruction) {
		this.rawInstruction = instruction;
		this.classifiedTokens = new ArrayList<>(16);
	}

	public Instruction classify() {
		classifyTokens();
		classifyInstruction();
		return new Instruction(classifiedTokens, instructionType);
	}

	private void classifyTokens() {
		for (String token : rawInstruction) {
			if (ParserConstants.keywords.contains(token)) {
				classifiedTokens.add(new Token(token, TokenTypes.KEYWORD));
			} else if (ParserConstants.operators.contains(token)) {
				classifiedTokens.add(new Token(token, TokenTypes.OPERATOR));
			} else if (ParserConstants.seperators.contains(token)) {
				classifiedTokens.add(new Token(token, TokenTypes.SEPERATOR));
			} else if (isContextKeyword(token)) {
				classifiedTokens.add(new Token(token, TokenTypes.KEYWORD));
			} else if (token.contains(".")) {
				classifiedTokens.add(new Token(token, TokenTypes.CHAINED_IDENTIFIER));
			} else {
				classifiedTokens.add(new Token(token, TokenTypes.IDENTIFIER));
			}
		}
	}

	private void classifyInstruction() {
		// TODO: make this function work ffs

		instructionType = InstructionTypes.OTHER;
	}

	public boolean isContextKeyword(String token) {
		// TODO: check if a token is a contextual keyword

		return false;
	}
}

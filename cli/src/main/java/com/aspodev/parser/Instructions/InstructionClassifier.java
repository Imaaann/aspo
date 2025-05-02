package com.aspodev.parser.Instructions;

import java.util.ArrayList;
import java.util.List;

import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.TokenNotFoundException;

public class InstructionClassifier {
	private List<String> rawInstruction;
	private List<Token> classifiedTokens;
	private InstructionTypes instructionType;

	public InstructionClassifier(List<String> instruction) {
		this.rawInstruction = instruction;
		this.classifiedTokens = new ArrayList<>(16);
	}

	public Instruction classify(ParserContext context) {
		classifiedTokens = classifyTokens(context);
		instructionType = classifyInstruction(context);
		return new Instruction(classifiedTokens, instructionType);
	}

	private List<Token> classifyTokens(ParserContext context) {
		List<Token> result = new ArrayList<>(24);

		for (int i = 0; i < rawInstruction.size(); i++) {
			result.add(new Token(rawInstruction.get(i), i));
		}

		return result;
	}

	private boolean isRecordDeclaration() {
		System.out.println("atleast work");
		try {
			Token recordIdentifier = Instruction.getIdentifier(classifiedTokens, 0);

			if (!recordIdentifier.getValue().equals("record"))
				return false;

			int recordPosition = recordIdentifier.getPosition();

			if (!classifiedTokens.get(recordPosition + 1).isIdentifier()
					|| !classifiedTokens.get(recordPosition + 2).getValue().equals("("))
				return false;

			return true;
		} catch (TokenNotFoundException e) {
			return false;
		}

	}

	private InstructionTypes classifyInstruction(ParserContext context) {
		// TODO: make this function work ffs
		if (classifiedTokens.contains(new Token("import")))
			return InstructionTypes.IMPORT_STATEMENT;

		if (classifiedTokens.contains(new Token("package")))
			return InstructionTypes.PACKAGE_STATEMENT;

		if (isRecordDeclaration())
			return InstructionTypes.RECORD_DEFINITION;

		return InstructionTypes.OTHER;
	}
}

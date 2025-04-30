package com.aspodev.parser.Instructions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.aspodev.parser.Token;

public class InstructionClassifier {
	private List<String> rawInstruction;
	private List<Token> classifiedTokens;
	private InstructionTypes instructionType;

	public InstructionClassifier(List<String> instruction) {
		this.rawInstruction = instruction;
		this.classifiedTokens = new ArrayList<>(16);
	}

	public Instruction classify() {
		classifiedTokens = classifyTokens();
		instructionType = classifyInstruction();
		return new Instruction(classifiedTokens, instructionType);
	}

	private List<Token> classifyTokens() {
		return rawInstruction.stream().map(token -> new Token(token)).collect(Collectors.toList());
	}

	private InstructionTypes classifyInstruction() {
		// TODO: make this function work ffs
		if (classifiedTokens.contains(new Token("import")))
			return InstructionTypes.IMPORT_STATEMENT;

		if (classifiedTokens.contains(new Token("package")))
			return InstructionTypes.PACKAGE_STATEMENT;

		return InstructionTypes.OTHER;
	}
}

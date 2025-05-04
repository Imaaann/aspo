package com.aspodev.parser.Instructions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.TokenNotFoundException;

import com.aspodev.parser.Scope.ScopeEnum;

public class InstructionClassifier {
	private List<String> rawInstruction;
	private List<Token> classifiedTokens;
	private Instruction instruction;

	public InstructionClassifier(List<String> instruction) {
		this.rawInstruction = instruction;
		this.classifiedTokens = new ArrayList<>(16);
	}

	public Instruction classify(ParserContext context) {
		classifiedTokens = classifyTokens(context);

		instruction = new Instruction(classifiedTokens);
		instruction.setType(classifyInstruction(context));

		return instruction;
	}

	private List<Token> classifyTokens(ParserContext context) {
		List<Token> result = new ArrayList<>(24);

		for (int i = 0; i < rawInstruction.size(); i++) {
			result.add(new Token(rawInstruction.get(i), i));
		}

		return result;
	}

	private InstructionTypes classifyInstruction(ParserContext context) {
		// TODO: make this function work ffs
		if (classifiedTokens.contains(new Token("import")))
			return InstructionTypes.IMPORT_STATEMENT;

		if (classifiedTokens.contains(new Token("package")))
			return InstructionTypes.PACKAGE_STATEMENT;

		if (isAttribute(context))
			return InstructionTypes.ATTRIBUTE_DECLARATION;

		if (isRecordDeclaration())
			return InstructionTypes.RECORD_DEFINITION;
		if (isLocalVariable(context))
			return InstructionTypes.LOCALVARIABLE_DECLARATION;

		return InstructionTypes.OTHER;
	}

	// #region Declaration functions

	private boolean isAttribute(ParserContext context) {
		if (context.getCurrentScope() == ScopeEnum.CLASS) {
			Iterator<Token> iterator = classifiedTokens.iterator();
			while (iterator.hasNext()) {
				Token token = iterator.next();
				if (token.isIdentifier() && iterator.hasNext()) {
					token = iterator.next();
					if (token.getValue().contains("<")) {
						InstructionUtil.getGenericHeader(iterator, token);
						if (iterator.hasNext()) {
							token = iterator.next();
						}
					}

					return token.isIdentifier();

				}
			}
		}
		return false;
	}

	private boolean isRecordDeclaration() {
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

	private boolean isLocalVariable(ParserContext context) {
		if (context.getCurrentScope() == ScopeEnum.INSTRUCTION) {
			Iterator<Token> iterator = classifiedTokens.iterator();
			while (iterator.hasNext()) {
				Token token = iterator.next();
				if (token.isIdentifier() && iterator.hasNext()) {
					token = iterator.next();
					if (token.getValue().contains("<")) {
						InstructionUtil.getGenericHeader(iterator, token);
						if (iterator.hasNext()) {
							token = iterator.next();
						}
					}

					return token.isIdentifier();

				}
			}
		}
		return false;
	}

	// #endregion
}
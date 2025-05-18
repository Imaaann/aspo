package com.aspodev.parser.Instructions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aspodev.TypeParser.TypeTokenEnum;
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
			result.add(new Token(rawInstruction.get(i), i, context));
		}

		return result;
	}

	private InstructionTypes classifyInstruction(ParserContext context) {
		if (isStaticImport())
			return InstructionTypes.STATIC_IMPORT_STATEMENT;

		if (classifiedTokens.contains(new Token("import")))
			return InstructionTypes.IMPORT_STATEMENT;

		if (classifiedTokens.contains(new Token("package")))
			return InstructionTypes.PACKAGE_STATEMENT;

		if (classifiedTokens.contains(new Token("class")))
			return InstructionTypes.CLASS_DECLARATION;

		if (classifiedTokens.contains(new Token("enum")))
			return InstructionTypes.ENUM_DECLARTION;

		if (classifiedTokens.contains(new Token("interface")))
			return InstructionTypes.INTERFACE_DECLARATION;

		if (isRecordDeclaration())
			return InstructionTypes.RECORD_DECLARATION;

		if (isConstructor(context))
			return InstructionTypes.CONSTRUCTOR_DEFENITION;

		if (isEnumerator(context)) {
			return InstructionTypes.ENUMERATOR_DECLARATION;
		}

		if (isGenericMethod(context)) {
			return InstructionTypes.GENERIC_METHOD_DECLARATION;
		}

		if (isMethod(context))
			return InstructionTypes.METHOD_DECLARATION;

		if (isAttribute(context))
			return InstructionTypes.ATTRIBUTE_DECLARATION;

		if (classifiedTokens.contains(new Token("catch")))
			return InstructionTypes.CATCH_STATEMENT;

		if (isAnonymousClass(context))
			return InstructionTypes.ANONYMOUS_CLASS_DECLARATION;

		if (isLabelCase(context))
			return InstructionTypes.LABEL_CASE_STATEMENT;

		if (isLocalVariable(context)) {
			return InstructionTypes.LOCAL_VARIABLE_DECLARATION;
		}

		if (classifiedTokens.get(0).getValue().equals("{") && context.getCurrentScope() == ScopeEnum.CLASS)
			return InstructionTypes.INITIALAZATION_BLOCK;

		if (classifiedTokens.get(0).getValue().equals("static") && classifiedTokens.get(1).getValue().equals("{")
				&& context.getCurrentScope() == ScopeEnum.CLASS)
			return InstructionTypes.STATIC_INITIALZATION;

		if (classifiedTokens.get(0).getValue().equals("}"))
			return InstructionTypes.END_OF_BLOCK;

		if (classifiedTokens.contains(new Token("switch")))
			return InstructionTypes.SWITCH_STATEMENT;

		if (classifiedTokens.contains(new Token("throw")))
			return InstructionTypes.THROW_STATEMENT;

		if (classifiedTokens.contains(new Token("->")))
			return InstructionTypes.LAMBDA_FUNCTION;

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
		ScopeEnum scope = context.getCurrentScope();

		if (scope != ScopeEnum.INSTRUCTION && scope != ScopeEnum.SWITCH_STATEMENT)
			return false;

		try {

			Token typeIdf = Instruction.getIdentifier(classifiedTokens, 0);
			int typeLength = InstructionUtil.resolveTypeLength(classifiedTokens, typeIdf.getPosition());

			Token nameIdf = classifiedTokens.get(typeLength + 1);

			return nameIdf.isIdentifier();
		} catch (Throwable e) {
			return false;
		}

	}

	private boolean isMethod(ParserContext context) {
		if (context.getCurrentScope() == ScopeEnum.CLASS) {
			Iterator<Token> iterator = classifiedTokens.iterator();
			Token token;
			while (iterator.hasNext()) {
				token = iterator.next();
				if (token.isIdentifier() && iterator.hasNext()) {
					token = iterator.next();
					if (token.getValue().contains("<")) {
						InstructionUtil.getGenericHeader(iterator, token);
						if (iterator.hasNext()) {
							token = iterator.next();
						}
					}

					return token.isIdentifier() && iterator.next().getValue().equals("(");

				}
			}
		}
		return false;
	}

	private boolean isConstructor(ParserContext context) {
		if (context.getCurrentScope() == ScopeEnum.CLASS) {
			Iterator<Token> iterator = classifiedTokens.iterator();
			Token token;
			Token lastToken = classifiedTokens.get(classifiedTokens.size() - 1);
			String className = context.getSlice().getMetaData().name();
			while (iterator.hasNext()) {
				token = iterator.next();
				if (token.getValue().equals(className)) {
					token = iterator.next();
					return token.getValue().equals("(") && lastToken.getValue().equals("{");
				}
			}
		}
		return false;
	}

	private boolean isGenericMethod(ParserContext context) {
		if (context.getCurrentScope() == ScopeEnum.CLASS) {
			try {

				Token idf = Instruction.getIdentifier(classifiedTokens, 0);
				Token preToken = classifiedTokens.get(idf.getPosition() - 1);

				if (preToken.getValue().contains("<"))
					return true;

			} catch (Throwable e) {
				return false;
			}

		}
		return false;
	}

	private boolean isEnumerator(ParserContext context) {
		if (context.getCurrentScope() != ScopeEnum.CLASS)
			return false;

		if (context.getSlice().getMetaData().type() != TypeTokenEnum.ENUM)
			return false;

		try {

			Token idf = Instruction.getIdentifier(classifiedTokens, 0);
			String nextToken = classifiedTokens.get(idf.getPosition() + 1).getValue();

			if (nextToken.equals("(") || nextToken.equals(",") || nextToken.equals("{"))
				return true;

		} catch (Throwable e) {
			return false;
		}

		return false;
	}

	private boolean isLabelCase(ParserContext context) {
		if (context.getCurrentScope() != ScopeEnum.SWITCH_STATEMENT)
			return false;

		String firstValue = classifiedTokens.get(0).getValue();
		return (firstValue.equals("case") || firstValue.equals("default"))
				&& classifiedTokens.contains(new Token("->"));
	}

	private boolean isStaticImport() {
		if (classifiedTokens.size() < 2)
			return false;

		String value1 = classifiedTokens.get(0).getValue();
		String value2 = classifiedTokens.get(1).getValue();
		return value1.equals("import") && value2.equals("static");
	}

	private boolean isAnonymousClass(ParserContext context) {

		List<Token> toks = classifiedTokens;
		int n = toks.size();
		if (n < 3)
			return false;

		// 1) Must end with '{'
		if (!"{".equals(toks.get(n - 1).getValue())) {
			return false;
		}

		// 2) Find last 'new'
		int newIdx = toks.lastIndexOf(new Token("new"));
		if (newIdx < 0 || newIdx + 1 >= n) {
			return false;
		}

		// 3) Next token must be identifier (the type)
		Token ctorName = toks.get(newIdx + 1);
		if (!ctorName.isIdentifier()) {
			return false;
		}

		// 4) Skip any generics on the type
		int idx = InstructionUtil.resolveTypeLength(toks, newIdx + 1);
		if (idx < 0 || idx >= n) {
			return false;
		}

		// 5) Find the '(' that starts the constructor args
		int parenStart = -1;
		for (int i = idx; i < n; i++) {
			if ("(".equals(toks.get(i).getValue())) {
				parenStart = i;
				break;
			}
			// if we hit the final '{' first, bail out
			if ("{".equals(toks.get(i).getValue())) {
				return false;
			}
		}
		if (parenStart < 0) {
			return false;
		}

		// 6) Track depth until we find the matching ')'
		int depth = 0;
		int matchIdx = -1;
		for (int i = parenStart; i < n; i++) {
			String v = toks.get(i).getValue();
			if ("(".equals(v)) {
				depth++;
			} else if (")".equals(v)) {
				depth--;
				if (depth == 0) {
					matchIdx = i;
					break;
				}
			}
		}
		if (matchIdx < 0) {
			return false;
		}

		// 7) That must be *exactly* the token before the '{'
		return matchIdx == n - 2;
	}

	// #endregion
}
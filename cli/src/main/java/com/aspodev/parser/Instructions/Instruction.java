package com.aspodev.parser.Instructions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.aspodev.SCAR.Accessors;
import com.aspodev.SCAR.Modifier;
import com.aspodev.TypeParser.TypeToken;
import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.TokenNotFoundException;

public class Instruction {
	private final List<Token> tokens;
	private InstructionTypes type;

	public Instruction(List<Token> tokens) {
		this.tokens = tokens;
	}

	public boolean contains(Token token) {
		return tokens.contains(token);
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

	public Token getToken(int index) {
		if (index >= tokens.size() || index < 0)
			return new Token("");

		return tokens.get(index);
	}

	public Token getToken(String name) {
		int tokenIndex = tokens.indexOf(new Token(name));

		if (tokenIndex == -1)
			return null;

		return tokens.get(tokenIndex);
	}

	public List<Modifier> getModifiers() {
		List<Modifier> result = new ArrayList<>();
		boolean normalClassDeclaration = isTypeDeclaration();
		boolean nonSealed = false;

		for (Token token : tokens) {
			if (isModifier(token))
				result.add(Modifier.convert(token.getValue()));

			if (normalClassDeclaration) {
				if (token.getValue().equals("sealed") && !nonSealed)
					result.add(Modifier.SEALED);

				if (token.getValue().equals("non")) {
					int nonPosition = token.getPosition();
					nonSealed = true;
					if (tokens.get(nonPosition + 1).getValue().equals("-")
							&& tokens.get(nonPosition + 2).getValue().equals("sealed")) {
						result.add(Modifier.NON_SEALED);
					}
				}
			}
		}

		return result;
	}

	public Accessors getAccessor() {
		for (Token token : tokens) {
			if (isAccessor(token))
				return Accessors.convert(token.getValue());
		}

		return Accessors.DEFAULT;
	}

	public List<Token> getTokens() {
		return this.tokens;
	}

	public Token getParentClassName(ParserContext context) {
		if (!this.isTypeDeclaration())
			return null;

		Token extendsToken = this.getToken("extends");

		if (extendsToken != null) {
			Token parentToken = this.getToken(extendsToken.getPosition() + 1);
			TypeToken parentType = context.getTypeToken(parentToken.getValue());
			System.out.println("[DEBUG] parent type token: \n" + parentType);
			Token result = new Token(parentType == null ? parentToken.getValue() : parentType.getFullName());
			System.out.println("[DEBUG] result token" + result);
			return result;
		}

		return null;
	}

	public List<Token> getInterfaceNames() {
		if (!this.isTypeDeclaration())
			return null;

		return InstructionUtil.getCommaSeperatedList(this, "implements");
	}

	public List<Token> getPermittedNames() {
		if (!this.isTypeDeclaration())
			return null;

		return InstructionUtil.getCommaSeperatedList(this, "permits");
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
		String instrString = tokens.stream().map(Token::toString).collect(Collectors.joining(""));
		return instrString + " | TYPE:" + type;
	}

	private boolean isTypeDeclaration() {
		return type == InstructionTypes.CLASS_DECLARATION || type == InstructionTypes.RECORD_DECLARATION
				|| type == InstructionTypes.ENUM_DECLARTION || type == InstructionTypes.INTERFACE_DECLARATION;
	}

	private boolean isModifier(Token token) {
		String value = token.getValue();

		return value.equals("abstract") || value.equals("synchronized") || value.equals("transient")
				|| value.equals("final") || value.equals("static") || value.equals("strictfp")
				|| value.equals("volatile");
	}

	private boolean isAccessor(Token token) {
		String value = token.getValue();

		return value.equals("public") || value.equals("private") || value.equals("protected");
	}
}

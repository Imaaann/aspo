package com.aspodev.parser;

import java.util.regex.Pattern;

public class Token {
	private String value;
	private TokenTypes type;
	private int position;

	public Token(String value, TokenTypes type, int position) {
		this.value = value;
		this.type = type;
		this.position = position;
	}

	public Token(String value, int position) {
		this.value = value;
		this.position = position;
		this.type = classifyToken(value);
	}

	public Token(String value) {
		this(value, 0);
	}

	public boolean isIdentifier() {
		return type == TokenTypes.IDENTIFIER || type == TokenTypes.CHAINED_IDENTIFIER
				|| type == TokenTypes.TYPE_IDENTIFIER;
	}

	public String getValue() {
		return value;
	}

	public int getPosition() {
		return position;
	}

	public TokenTypes getType() {
		return type;
	}

	public void setType(TokenTypes type) {
		this.type = type;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setPosition(int pos) {
		this.position = pos;
	}

	private TokenTypes classifyToken(String token) {
		if (ParserConstants.keywords.contains(token)) {
			return TokenTypes.KEYWORD;
		} else if (ParserConstants.operators.contains(token)) {
			return TokenTypes.OPERATOR;
		} else if (ParserConstants.seperators.contains(token)) {
			return TokenTypes.SEPERATOR;
		} else if (token.contains(".")) {
			return TokenTypes.CHAINED_IDENTIFIER;
		} else if (isJavaNumber(token)) {
			return TokenTypes.LITERAL;
		} else {
			return TokenTypes.IDENTIFIER;
		}
	}

	public String toString() {
		return switch (type) {
			case KEYWORD:
				yield ("KW(" + value + ")");
			case CHAINED_IDENTIFIER:
				yield ("CID(" + value + ")");
			case IDENTIFIER:
				yield ("ID(" + value + ")");
			case LITERAL:
				yield ("LT(" + value + ")");
			case OPERATOR:
				yield ("OP(" + value + ")");
			case SEPERATOR:
				yield ("SP(" + value + ")");
			case TYPE_IDENTIFIER:
				yield ("TID(" + value + ")");
			default:
				throw new IllegalArgumentException("Unexpected value: " + type);
		};
	}

	public boolean equals(Object obj) {
		if (obj instanceof Token) {
			return ((Token) obj).getValue().equals(this.getValue());
		}

		return false;
	}

	public void append(Token token) {
		this.value = this.value + token.getValue();
	}

	private boolean isJavaNumber(String s) {
		if (s == null)
			return false;
		String integer = "(?:"
				+ "(?:0|[1-9](?:_[0-9]+)*)[lL]?"
				+ "|0[xX][0-9A-Fa-f]+(?:_[0-9A-Fa-f]+)*[lL]?"
				+ "|0[0-7]+(?:_[0-7]+)*[lL]?"
				+ "|0[bB][01]+(?:_[01]+)*[lL]?"
				+ ")";
		String decimalExp = "(?:[eE][+-]?[0-9]+(?:_[0-9]+)*)";
		String floatSuffix = "[fFdD]?";
		String decimalFloat = "(?:"
				+ "(?:[0-9]+(?:_[0-9]+)*)?\\.(?:[0-9]+(?:_[0-9]+)*)?"
				+ "(?:" + decimalExp + ")?"
				+ floatSuffix
				+ "|(?:[0-9]+(?:_[0-9]+)*" + decimalExp + floatSuffix + ")"
				+ "|(?:[0-9]+(?:_[0-9]+)*" + floatSuffix + ")"
				+ ")";
		String hexSignif = "(?:0[xX]"
				+ "(?:[0-9A-Fa-f]+(?:_[0-9A-Fa-f]+)*)?\\.[0-9A-Fa-f]+(?:_[0-9A-Fa-f]+)*"
				+ "|\\.[0-9A-Fa-f]+(?:_[0-9A-Fa-f]+)*"
				+ ")"
				+ "[pP][+-]?[0-9]+(?:_[0-9]+)*" + floatSuffix;
		String master = "^(?:" + integer + "|" + decimalFloat + "|" + hexSignif + ")$";

		return Pattern.matches(master, s);
	}
}

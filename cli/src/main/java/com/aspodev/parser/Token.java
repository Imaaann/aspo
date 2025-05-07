package com.aspodev.parser;

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
}

package com.aspodev.parser;

public record Token(String value, TokenTypes type) {
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
}

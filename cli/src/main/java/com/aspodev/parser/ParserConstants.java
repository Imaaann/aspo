package com.aspodev.parser;

import java.util.Set;

public class ParserConstants {
	public static Set<String> skippableTokens = Set.of("continue", "for", "if", "do", "goto", "break", "else", "return",
			"try", "finally", "const", "while", "!", "~", "==", ">=", "<=", "!=", "&&", "||", "++", "--", "+", "*", "/",
			"&", "|", "^", "%");

	public static Set<String> keywords = Set.of("abstract", "continue", "for", "new", "switch", "assert", "default",
			"if", "package", "synchronized", "do", "goto", "private", "this", "break", "implements", "protected",
			"throw", "else", "import", "public", "throws", "case", "enum", "instanceof", "return", "transient", "catch",
			"extends", "try", "final", "interface", "static", "class", "finally", "strictfp", "volatile", "const",
			"native", "super", "while", "_", "null", "true", "false");
	public static Set<String> operators = Set.of("=", "!", "~", "==", ">=", "<=", "!=", "&&", "||", "++", "--", "+",
			"-", "*", "/", "&", "|", "^", "%", "<<", ">>", ">>>", "+=", "-=", "*=", "/=", "&=", "|=", "^=", "%=", "<<=",
			">>=", ">>>=", "<", ">", "?", ":", "->");
	public static Set<String> seperators = Set.of("(", ")", "{", "}", "[", "]", ";", ",", ".", "...", "@", "::");

	public static Set<String> assignmentOperators = Set.of("+=", "-=", "*=", "/=", "&=", "|=", "^=", "%=", "<<=", ">>=",
			">>>=", "=");
}

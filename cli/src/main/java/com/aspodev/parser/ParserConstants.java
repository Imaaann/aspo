package com.aspodev.parser;

import java.util.Set;

public abstract class ParserConstants {
	public final static Set<String> keywords = Set.of("abstract", "continue", "for", "new", "switch", "assert",
			"default", "if", "package", "synchronized", "boolean", "do", "goto", "private", "this", "break",
			"implements", "protected", "throw", "else", "import", "public", "throws", "case", "enum", "instanceof",
			"return", "transient", "catch", "extends", "try", "final", "interface", "static", "void", "class",
			"finally", "long", "strictfp", "volatile", "const", "native", "super", "while", "_");
	public final static Set<String> modifiers = Set.of("final", "abstract", "static", "native", "synchronized",
			"transient", "volatile");
	public final static Set<String> accessors = Set.of("public", "protected", "private");
	public final static Set<String> operators = Set.of(">>>=", ">>>", "<<=", ">>=", "...", "->", "==", ">=", "<=", "!=",
			"&&", "||", "++", "--", "<<", ">>", "+=", "-=", "*=", "/=", "&=", "|=", "^=", "%=", "::", "=", ">", "<",
			"!", "~", "?", ":", "+", "-", "*", "/", "&", "|", "^", "%", "(", ")", "{", "}", "[", "]", ";", ",", ".",
			"@", "\"\"\"", "\"", "\'");
}

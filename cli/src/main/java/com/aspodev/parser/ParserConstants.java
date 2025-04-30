package com.aspodev.parser;

import java.util.Set;

public class ParserConstants {
	public static Set<String> skippableTokens = Set.of("continue", "for", "if", "do", "goto", "break", "else", "return",
			"try", "finally", "const", "while", "=", "!", "~", "==", ">=", "<=", "!=", "&&", "||", "++", "--", "+", "-",
			"*", "/", "&", "|", "^", "%", "<<", ">>", ">>>", "+=", "-=", "*=", "/=", "&=", "|=", "^=", "%=", "<<=",
			">>=", ">>>=");
}

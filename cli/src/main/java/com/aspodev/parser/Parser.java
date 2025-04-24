package com.aspodev.parser;

import java.nio.file.Path;
import java.util.List;

import com.aspodev.TypeParser.TypeParser;
import com.aspodev.TypeParser.TypeSpace;
import com.aspodev.tokenizer.Tokenizer;

public class Parser {
	private TypeSpace typeSpace;
	private TypeParser globalParser;
	private List<String> tokens;

	public Parser(Path path, TypeParser globalParser) {
		this.typeSpace = new TypeSpace();
		this.globalParser = globalParser;

		Tokenizer tokenizer = new Tokenizer(path);
		tokenizer.tokenize();
		this.tokens = tokenizer.getTokens();

	}

}

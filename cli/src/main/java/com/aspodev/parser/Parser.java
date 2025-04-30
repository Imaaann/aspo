package com.aspodev.parser;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

import com.aspodev.TypeParser.TypeParser;
import com.aspodev.tokenizer.Tokenizer;

public class Parser {
	private TypeParser parser;
	private List<String> tokens;
	private Iterator<String> iterator;

	public Parser(Path path, TypeParser parser) {
		this.parser = parser;

		Tokenizer tokenizer = new Tokenizer(path);
		tokenizer.tokenize();

		this.tokens = tokenizer.getTokens();
		this.iterator = tokens.iterator();
	}

	// TODO: replace with SCAR model as return
	public void parse() {

	}
}

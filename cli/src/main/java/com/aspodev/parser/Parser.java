package com.aspodev.parser;

import java.nio.file.Path;

import com.aspodev.TypeParser.TypeParser;
import com.aspodev.tokenizer.Tokenizer;

public class Parser {
	private ParserContext context;
	private BehaviorManager manager;

	public Parser(Path path, TypeParser globalParser) {
		Tokenizer tokenizer = new Tokenizer(path);
		tokenizer.tokenize();

		this.context = new ParserContext(tokenizer.getTokens(), globalParser);
		this.manager = BehaviorManager.getInstance();
	}
	// TODO: implement parsing code here

}

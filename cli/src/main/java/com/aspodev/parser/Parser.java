package com.aspodev.parser;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

import com.aspodev.TypeParser.TypeParser;
import com.aspodev.tokenizer.Tokenizer;

public class Parser {
	private ParserContext context;
	private final BehaviorManager manager;
	private List<String> tokens;
	private Iterator<String> iterator;

	public Parser(Path path, TypeParser globalParser) {
		Tokenizer tokenizer = new Tokenizer(path);
		tokenizer.tokenize();
		this.tokens = tokenizer.getTokens();
		this.iterator = tokens.iterator();
		this.context = new ParserContext(iterator, globalParser);
		this.manager = BehaviorManager.getInstance();
	}

	// TODO: Replace the void with the SCAR output eventually
	public void parse() {
		while (iterator.hasNext()) {
			String token = iterator.next();
			ParserBehaviors behavior = this.classify(token);
			manager.execute(behavior, context);
		}
	}

	public ParserBehaviors classify(String token) {
		if (token.equals("import"))
			return ParserBehaviors.IMPORT_STATEMENT;

		if (token.equals("package"))
			return ParserBehaviors.PACKAGE_STATEMENT;

		return ParserBehaviors.SKIP;
	}

}

package com.aspodev.parser;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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
			manager.execute(behavior, context, token);
		}

	}

	public ParserBehaviors classify(String token) {

		if (token.equals("import"))
			return ParserBehaviors.IMPORT_STATEMENT;

		if (token.equals("package"))
			return ParserBehaviors.PACKAGE_STATEMENT;

		if (token.equals("\"") || token.equals("\"\"\""))
			return ParserBehaviors.STRING_LITERAL;

		if (ParserConstants.modifiers.contains(token))
			return ParserBehaviors.MODIFIER;

		if (ParserConstants.accessors.contains(token))
			return ParserBehaviors.ACCESSOR;

		// TODO: Create the isIdentifier method, the behavior and the classification
		// method
		if (isIdentifier(token))
			return ParserBehaviors.IDENTIFIER;

		return ParserBehaviors.SKIP;
	}

	public boolean isIdentifier(String token) {
		if (ParserConstants.keywords.contains(token))
			return false;
		if (ParserConstants.operators.contains(token))
			return false;

		return true;
	}
}

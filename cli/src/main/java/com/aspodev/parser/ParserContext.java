package com.aspodev.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aspodev.TypeParser.TypeParser;
import com.aspodev.TypeParser.TypeSpace;

public class ParserContext {
	private TypeSpace typeSpace;
	private TypeParser globalParser;
	private List<String> tokens;
	private Iterator<String> iterator;
	private String identifier;
	private ArrayList<String> modifiers;

	public ParserContext(List<String> tokens, TypeParser globalParser) {
		this.typeSpace = new TypeSpace();
		this.globalParser = globalParser;
		this.tokens = tokens;
		this.iterator = tokens.iterator();
		this.modifiers = new ArrayList<>();
	}

	public void skip() {
		if (iterator.hasNext()) {
			// Debugging print system
			System.out.println("skipped: " + iterator.next());
		}
	}

}
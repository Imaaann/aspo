package com.aspodev.parser;

import java.util.ArrayList;
import java.util.Iterator;

import com.aspodev.TypeParser.TypeParser;
import com.aspodev.TypeParser.TypeSpace;
import com.aspodev.TypeParser.TypeTokenEnum;

public class ParserContext {
	private TypeSpace typeSpace;
	private TypeParser globalParser;
	private Iterator<String> iterator;
	private String identifier;
	private ArrayList<String> modifiers;

	public ParserContext(Iterator<String> tokensIterator, TypeParser globalParser) {
		this.typeSpace = new TypeSpace();
		this.globalParser = globalParser;
		this.iterator = tokensIterator;
		this.modifiers = new ArrayList<>();
		this.identifier = "";
	}

	public String getIdentifier() {
		String identifier = "";
		String token = "";

		/**
		 * TODO: make the loop escape on ANY reserved tokens in JAVA 17 except . also
		 * more shit, you can also probably make this a stream instead
		 */

		while (!token.equals(";")) {
			identifier += token;
			token = iterator.next();
		}

		return identifier;
	}

	public void addType(String typeName, String pkg, TypeTokenEnum type) {
		if (typeName.equals("*"))
			typeSpace.addWildCardPackage(pkg, globalParser);

		typeSpace.addType(typeName, pkg, type);
	}

}
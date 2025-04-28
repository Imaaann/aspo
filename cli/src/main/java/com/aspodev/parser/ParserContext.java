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

		// TODO: make this method 100% reliable to find identifiers and their type
		// (var/type)

		while (!token.equals(";")) {
			identifier += token;
			token = iterator.next();
		}

		return identifier;
	}

	public Iterator<String> getIterator() {
		return this.iterator;
	}

	public void addType(String typeName, String pkg, TypeTokenEnum type) {
		if (typeName.equals("*"))
			typeSpace.addWildCardPackage(pkg, globalParser);

		typeSpace.addType(typeName, pkg, type);
	}

	public void addPackage(String pkgName) {
		typeSpace.addPackage(pkgName, globalParser);
	}

	public String toString() {
		return "[TypeSpace]: " + typeSpace;
	}
}
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

	private ArrayList<String> modifiers;
	private String accessor;

	private Identifier identifier;
	private boolean identifierPending;
	private Behavior onIdentifierrBehavior;

	public ParserContext(Iterator<String> tokensIterator, TypeParser globalParser) {
		this.typeSpace = new TypeSpace();
		this.globalParser = globalParser;
		this.iterator = tokensIterator;
		this.modifiers = new ArrayList<>();
		this.accessor = "";
		this.identifierPending = false;
	}

	public Identifier getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {

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

	public void addModifier(String modifier) {
		modifiers.add(modifier);
		System.out.println("[DEBUG] Added: " + modifier);
	}

	public void setAccessor(String accessor) {
		this.accessor = accessor;
		System.out.println("[DEBUG] Added: " + accessor);
	}

	public void setIdentifierBehavior(Behavior behavior) {
		this.onIdentifierrBehavior = behavior;
	}

	public void identifierCaught() {
		this.onIdentifierrBehavior.apply(this, identifier.id());
	}

	public void setIdentifierPending(boolean val) {
		this.identifierPending = val;
	}

	public String toString() {
		return "[TypeSpace]: " + typeSpace;
	}
}
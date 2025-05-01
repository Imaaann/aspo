package com.aspodev.parser;

import com.aspodev.TypeParser.TypeParser;
import com.aspodev.TypeParser.TypeSpace;
import com.aspodev.TypeParser.TypeToken;

public class ParserContext {
	private TypeSpace space;
	private TypeParser parser;


	public ParserContext(TypeParser parser) {
		this.parser = parser;
		this.space = new TypeSpace();
	}

	public void addType(TypeToken type) {
		this.space.addType(type);
	}

	public void addPackage(String pkgName) {
		this.space.addPackage(pkgName, parser);
	}

	public boolean isType(Token token){
		return space.isType(token);
	}

	public String toString(){
		return space.toString();
	}
}

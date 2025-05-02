package com.aspodev.parser;

import com.aspodev.TypeParser.TypeParser;
import com.aspodev.TypeParser.TypeSpace;
import com.aspodev.TypeParser.TypeToken;
import com.aspodev.parser.Scope.Scope;
import com.aspodev.parser.Scope.ScopeEnum;

public class ParserContext {
	private TypeSpace space;
	private TypeParser parser;
	private Scope scope;

	public ParserContext(TypeParser parser) {
		this.parser = parser;
		this.space = new TypeSpace();
		this.scope = new Scope();
	}

	// #region Scope methods

	public void changeScope(ScopeEnum newScope) {
		scope.changeScope(newScope);
	}

	public void changeScope() {
		scope.changeScope();
	}

	public void rewindScope() {
		scope.rewindScope();
	}

	public int getScopeCount() {
		return scope.getScopeCount();
	}

	public ScopeEnum getCurrentScope() {
		return scope.getCurrentScope();
	}

	// #endregion

	// #region TypeSpace methods

	public void addType(TypeToken type) {
		this.space.addType(type);
	}

	public void addPackage(String pkgName) {
		this.space.addPackage(pkgName, parser);
	}

	// #endregion

}

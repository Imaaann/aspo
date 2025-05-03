package com.aspodev.parser;

import java.util.Stack;

import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;
import com.aspodev.TypeParser.TypeParser;
import com.aspodev.TypeParser.TypeSpace;
import com.aspodev.TypeParser.TypeToken;
import com.aspodev.parser.Scope.Scope;
import com.aspodev.parser.Scope.ScopeEnum;

public class ParserContext {
	private TypeSpace space;
	private TypeParser parser;
	private Scope scope;
	private Model model;
	private Stack<Slice> slices;

	public ParserContext(TypeParser parser, Model model) {
		this.model = model;
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

	public boolean isType(Token token) {
		return space.isType(token);
	}

	public String toString() {
		return space.toString();
	}

	// #endregion

	// #region SCAR methods

	public void createSlice(TypeToken data) {
		if (slices.empty())
			slices.push(new Slice(data));
		else
			slices.push(new Slice(data, this.getSlice().getMetaData().getFullName()));
	}

	public Slice getSlice() {
		return slices.peek();
	}

	public void finalizeSlice() {
		if (slices.empty())
			return;

		Slice current = slices.pop();
		model.addSlice(current);
	}

	// #endregion

}

package com.aspodev.parser;

import java.util.Stack;

import com.aspodev.SCAR.Model;
import com.aspodev.SCAR.Slice;
import com.aspodev.SCAR.Method;
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

	private Method currentMethod;
	private LocalVariableMap localVariables;

	public ParserContext(TypeParser parser, Model model) {
		this.model = model;
		this.parser = parser;
		this.space = new TypeSpace();
		this.scope = new Scope();
		this.localVariables = new LocalVariableMap();
	}

	// #region Scope methods

	public void changeScope(ScopeEnum newScope) {
		scope.changeScope(newScope);
	}

	public void changeScope() {
		scope.changeScope();
	}

	/**
	 * Rewinds the scope back to the previous state, also destroys any
	 * localVariables found in that scope
	 */
	public void rewindScope() {
		localVariables.removeScope(getScopeCount());
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

	// #region Other Methods

	public void setMethod(Method method) {
		this.currentMethod = method;
	}

	public Method getCurrentMethod() {
		return this.currentMethod;
	}

	public void addDependency(String methodName, String callerType) {
		this.currentMethod.addDependency(methodName, callerType);
	}

	public void addLocalVariable(String typeName, String varName) {
		this.localVariables.addVariable(getScopeCount(), typeName, varName);
	}

	public String getVariableType(String varName) {
		return this.localVariables.getVariableType(getScopeCount(), varName);
	}

	// #endregion
}

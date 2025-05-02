package com.aspodev.parser.Scope;

import java.util.Stack;

public class Scope {
	private ScopeEnum currentScope;
	private int scopeCount;
	private Stack<ScopeEnum> stateStack;

	public Scope() {
		currentScope = ScopeEnum.GLOBAL;
		scopeCount = 0;
		stateStack = new Stack<>();
	}

	/**
	 * @param newScope the scope to switch to, increases the scopeCount
	 */
	public void changeScope(ScopeEnum newScope) {
		stateStack.push(currentScope);
		scopeCount++;
		currentScope = newScope;
	}

	/**
	 * increases the scope count without switching the context
	 */
	public void changeScope() {
		scopeCount++;
	}

	public void rewindScope() {
		currentScope = stateStack.pop();
		scopeCount--;
	}

	public int getScopeCount() {
		return scopeCount;
	}

	public ScopeEnum getCurrentScope() {
		return currentScope;
	}
}

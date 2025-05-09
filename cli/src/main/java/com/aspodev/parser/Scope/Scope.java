package com.aspodev.parser.Scope;

import java.util.Stack;

public class Scope {
	private Stack<ScopeEnum> stateStack;

	public Scope() {
		stateStack = new Stack<>();
		stateStack.push(ScopeEnum.GLOBAL);
	}

	/**
	 * @param newScope the scope to switch to, increases the scopeCount
	 */
	public void changeScope(ScopeEnum newScope) {
		stateStack.push(newScope);
	}

	public void rewindScope() {
		if (stateStack.empty()) {
			stateStack.push(ScopeEnum.GLOBAL);
		}

		stateStack.pop();
	}

	public ScopeEnum getCurrentScope() {
		if (stateStack.empty())
			return ScopeEnum.GLOBAL;

		return stateStack.peek();
	}

	public int getScopeCount() {
		if (stateStack.empty())
			return 0;

		return stateStack.size() - 1;
	}

}

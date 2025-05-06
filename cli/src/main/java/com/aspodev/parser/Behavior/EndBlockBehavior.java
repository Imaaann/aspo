package com.aspodev.parser.Behavior;

import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Instructions.Instruction;
import com.aspodev.parser.Scope.ScopeEnum;

public class EndBlockBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {

		ScopeEnum currentScope = context.getCurrentScope();

		// TODO: finish this after being done with all the class members behaviors

		switch (currentScope) {
		case GLOBAL:
		case CLASS:
		default:
		}

		context.rewindScope();

		System.out.println("[DEBUG] == switched back to: " + context.getCurrentScope());

	}

}

package com.aspodev.parser.Behavior;

import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Instructions.Instruction;
import com.aspodev.parser.Scope.ScopeEnum;

public class EndBlockBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {

		ScopeEnum currentScope = context.getCurrentScope();

		switch (currentScope) {
		case GLOBAL:
			// case leaving the global scope, output a warning and do nothing
			System.out.println("[WARN] Parser trying to leave GLOBAL scope: " + context.getClassName());
		case CLASS:
			// case leaving a class declaration, need to finalize class into Model
			context.finalizeSlice();
		default:
			// case leaving a method/code block, delete local variables in that scope
			context.deleteScopeVariables();
		}

		context.rewindScope();
	}

}

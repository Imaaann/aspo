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
			// TODO: yea this one is an issue tbh if u hit this case problem
		case CLASS:
			// TODO: if leaving the class scope finalize the class by adding it to the model
		default:
			// TODO: if leaving the INSTUCTION OR SWITCH scope remove the variable
			// declarations inside
		}

		context.rewindScope();

		System.out.println("[DEBUG] == switched back to: " + context.getCurrentScope());

	}

}

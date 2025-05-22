package com.aspodev.parser.Behavior;

import com.aspodev.SCAR.Accessors;
import com.aspodev.SCAR.Method;
import com.aspodev.SCAR.Slice;
import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Instructions.Instruction;
import com.aspodev.parser.Scope.ScopeEnum;

public class InitializerBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		Method initializerMethod = new Method("special.initializer", "void", Accessors.PRIVATE, "none");
		context.setMethod(initializerMethod);
		Slice currentSlice = context.getSlice();

		if (currentSlice == null) {
			return;
		}

		currentSlice.addMethod(initializerMethod);
		context.changeScope(ScopeEnum.INSTRUCTION);
	}

}

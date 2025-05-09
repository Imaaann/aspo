package com.aspodev.parser.Behavior;

import com.aspodev.SCAR.Accessors;
import com.aspodev.SCAR.Method;
import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Instructions.Instruction;
import com.aspodev.parser.Scope.ScopeEnum;

public class StaticInitBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		Method staticInitMethod = new Method("special.static.initializer", "void", Accessors.PRIVATE, "none");
		staticInitMethod.addModifier("static");
		context.setMethod(staticInitMethod);
		context.getSlice().addMethod(staticInitMethod);
		context.changeScope(ScopeEnum.INSTRUCTION);
	}

}

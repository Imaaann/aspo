package com.aspodev.parser.Behavior;

import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Instructions.Instruction;
import com.aspodev.parser.Scope.ScopeEnum;

public class SwitchBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		new OtherBehavior().apply(context, instruction);
		context.rewindScope();
		context.changeScope(ScopeEnum.SWITCH_STATEMENT);
		System.out.println("[DEBUG] Switched to scope" + context.getCurrentScope());
	}

}

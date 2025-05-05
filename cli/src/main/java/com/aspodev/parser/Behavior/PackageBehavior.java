package com.aspodev.parser.Behavior;

import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.Instructions.Instruction;

public class PackageBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		Token identifier = instruction.getIdentifier(0);
		context.addPackage(identifier.getValue());
		context.setPackage(identifier.getValue());
	}

}

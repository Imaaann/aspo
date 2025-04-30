package com.aspodev.parser.Behavior;

import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Instructions.Instruction;

@FunctionalInterface
public interface Behavior {
	public abstract void apply(ParserContext context, Instruction instruction);
}

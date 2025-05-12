package com.aspodev.parser.Behavior;

import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.Instructions.Instruction;
import com.aspodev.parser.Instructions.InstructionUtil;
import com.aspodev.parser.Scope.ScopeEnum;

public class CatchBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		Token exceptionType = instruction.getIdentifier(0);
		InstructionUtil.resolveType(instruction.getTokens(), exceptionType.getPosition());
		Token exceptionVarName = instruction.getToken(exceptionType.getPosition() + 1);
		context.getSlice().addHandledException(exceptionType.getValue());
		context.addLocalVariable(exceptionType.getValue(), exceptionVarName.getValue());

		if (instruction.contains(new Token("{")))
			context.changeScope(ScopeEnum.INSTRUCTION);

	}

}

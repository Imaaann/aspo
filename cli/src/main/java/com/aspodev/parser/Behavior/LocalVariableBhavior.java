package com.aspodev.parser.Behavior;

import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.Instructions.Instruction;
import com.aspodev.parser.Instructions.InstructionUtil;
import com.aspodev.parser.Scope.ScopeEnum;

public class LocalVariableBhavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		Token typeName = instruction.getIdentifier(0);
		typeName = InstructionUtil.resolveType(instruction.getTokens(), typeName.getPosition());

		Token varName = instruction.getToken(typeName.getPosition() + 1);
		context.addLocalVariable(typeName.getValue(), varName.getValue());

		if (instruction.contains(new Token("{"))) {
			context.changeScope(ScopeEnum.INSTRUCTION);
		}

	}

}

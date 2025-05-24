package com.aspodev.parser.Behavior;

import java.util.List;

import com.aspodev.SCAR.Accessors;
import com.aspodev.SCAR.Attribute;
import com.aspodev.SCAR.Modifier;
import com.aspodev.SCAR.Slice;
import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.Instructions.Instruction;
import com.aspodev.parser.Instructions.InstructionUtil;

public class AttributeBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		List<Modifier> modifiers = instruction.getModifiers();
		Accessors accessor = instruction.getAccessor();

		Token typeName = instruction.getIdentifier(0);
		InstructionUtil.resolveType(instruction.getTokens(), typeName.getPosition());

		List<Token> varNames = InstructionUtil.getCommaSeperatedList(instruction, typeName.getPosition() + 1);
		Slice currentSlice = context.getSlice();

		if (currentSlice == null) {
			return;
		}

		for (Token var : varNames) {
			Attribute attribute = new Attribute(var.getValue(), typeName.getValue(), accessor);
			attribute.addModifier(modifiers);

			currentSlice.addAttribute(attribute);
			context.addLocalVariable(typeName.getValue(), var.getValue());
		}
	}

}

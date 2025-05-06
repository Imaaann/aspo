package com.aspodev.parser.Behavior;

import java.util.Iterator;
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
		int typeNamePos = typeName.getPosition();

		List<Token> tokens = instruction.getTokens();
		Token varName = null;

		if (instruction.getToken(typeNamePos + 1).getValue().contains("<")) {

			Iterator<Token> iterator = tokens.iterator();

			Token temp = new Token("");
			while (!temp.getValue().contains("<")) {
				temp = iterator.next();
			}

			Token genericHeader = InstructionUtil.getGenericHeader(iterator, temp);

			if (iterator.hasNext()) {
				varName = iterator.next();
				typeName.append(genericHeader);
			}

		} else {
			varName = instruction.getIdentifier(1);
		}

		Attribute attribute = new Attribute(varName.getValue(), typeName.getValue(), accessor);
		attribute.addModifier(modifiers);

		Slice currentSlice = context.getSlice();

		currentSlice.addAttribute(attribute);
	}

}

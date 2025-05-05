package com.aspodev.parser.Behavior;

import java.util.List;

import com.aspodev.SCAR.Accessors;
import com.aspodev.SCAR.Modifier;
import com.aspodev.SCAR.Slice;
import com.aspodev.TypeParser.TypeToken;
import com.aspodev.TypeParser.TypeTokenEnum;
import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.Instructions.Instruction;
import com.aspodev.parser.Instructions.InstructionUtil;
import com.aspodev.parser.Scope.ScopeEnum;

public class EnumBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		List<Modifier> modifiers = instruction.getModifiers();
		Accessors accessors = instruction.getAccessor();

		int enumIndex = InstructionUtil.getClassNamePosition(modifiers);
		Token enumName = instruction.getIdentifier(enumIndex);

		context.createSlice(new TypeToken(enumName.getValue(), context.getPackage(), TypeTokenEnum.ENUM));

		Slice enumSlice = context.getSlice();

		List<Token> interfaceList = instruction.getInterfaceNames();

		enumSlice.setAccessor(accessors);
		enumSlice.addModifier(modifiers);

		if (interfaceList != null)
			enumSlice.addInterface(interfaceList.stream().map(t -> t.getValue()).toList());

		context.changeScope(ScopeEnum.CLASS);

	}

}

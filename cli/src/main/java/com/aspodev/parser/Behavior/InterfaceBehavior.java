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

public class InterfaceBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		List<Modifier> modifiers = instruction.getModifiers();
		Accessors accessor = instruction.getAccessor();
		int interfaceNamePosition = InstructionUtil.getClassNamePosition(modifiers);
		Token interfaceName = instruction.getIdentifier(interfaceNamePosition);

		context.createSlice(new TypeToken(interfaceName.getValue(), context.getPackage(), TypeTokenEnum.INTERFACE));
		context.setClassName(interfaceName.getValue() + "." + context.getPackage());

		Slice interfaceSlice = context.getSlice();

		interfaceSlice.setAccessor(accessor);
		interfaceSlice.addModifier(modifiers);

		Token parentClassToken = instruction.getParentClassName(context);
		List<Token> interfaceList = instruction.getInterfaceNames(context);
		List<Token> permitsList = instruction.getPermittedNames();

		if (parentClassToken != null) {
			interfaceSlice.setParentName(parentClassToken.getValue());
		}

		if (interfaceList != null)
			interfaceSlice.addInterface(interfaceList.stream().map(t -> t.getValue()).toList());

		if (permitsList != null)
			interfaceSlice.addPermits(permitsList.stream().map(t -> t.getValue()).toList());

		context.changeScope(ScopeEnum.CLASS);
		context.addLocalVariable(interfaceName.getValue(), "this");
		if (parentClassToken != null)
			context.addLocalVariable(parentClassToken.getValue(), "super");
	}

}

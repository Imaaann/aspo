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

public class ClassBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		List<Modifier> modifiers = instruction.getModifiers();
		Accessors accessor = instruction.getAccessor();

		int classNameIndex = InstructionUtil.getClassNamePosition(modifiers);
		Token className = instruction.getIdentifier(classNameIndex);

		context.createSlice(new TypeToken(className.getValue(), context.getPackage(), TypeTokenEnum.CLASS));
		context.setClassName(className.getValue() + "." + context.getPackage());

		Slice classSlice = context.getSlice();

		Token parentClassToken = instruction.getParentClassName(context);
		List<Token> interfaceList = instruction.getInterfaceNames(context);
		List<Token> permitsList = instruction.getPermittedNames();

		classSlice.setAccessor(accessor);
		classSlice.addModifier(modifiers);

		if (parentClassToken != null) {
			classSlice.setParentName(parentClassToken.getValue());
		}

		if (interfaceList != null) {
			classSlice.addInterface(interfaceList.stream().map(t -> t.getValue()).toList());
		}

		if (permitsList != null)
			classSlice.addPermits(permitsList.stream().map(t -> t.getValue()).toList());

		context.changeScope(ScopeEnum.CLASS);
		context.addLocalVariable(className.getValue(), "this");
		if (parentClassToken != null)
			context.addLocalVariable(parentClassToken.getValue(), "super");

	}

}

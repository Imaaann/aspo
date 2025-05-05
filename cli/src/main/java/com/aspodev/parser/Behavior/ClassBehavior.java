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
import com.aspodev.parser.Scope.ScopeEnum;

public class ClassBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		List<Modifier> modifiers = instruction.getModifiers();
		Accessors accessor = instruction.getAccessor();

		int classNameIndex = 0;

		if (modifiers.contains(Modifier.NON_SEALED)) {
			classNameIndex = 2;
		} else if (modifiers.contains(Modifier.SEALED)) {
			classNameIndex = 1;
		}

		Token className = instruction.getIdentifier(classNameIndex);

		context.createSlice(new TypeToken(className.getValue(), context.getPackage(), TypeTokenEnum.CLASS));

		Slice classSlice = context.getSlice();

		Token parentClassToken = instruction.getParentClassName();
		List<Token> interfaceList = instruction.getInterfaceNames();

		classSlice.setAccessor(accessor);

		if (parentClassToken != null) {
			classSlice.setParentName(parentClassToken.getValue());
		}

		if (interfaceList != null) {
			classSlice.addInterface(interfaceList.stream().map(t -> t.getValue()).toList());
		}

		context.changeScope(ScopeEnum.CLASS);
	}

}

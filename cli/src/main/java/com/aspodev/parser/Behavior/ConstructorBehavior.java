package com.aspodev.parser.Behavior;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aspodev.SCAR.Accessors;
import com.aspodev.SCAR.Method;
import com.aspodev.SCAR.Modifier;
import com.aspodev.SCAR.Slice;
import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.Instructions.Instruction;
import com.aspodev.parser.Instructions.InstructionUtil;
import com.aspodev.parser.Scope.ScopeEnum;

public class ConstructorBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		List<Modifier> modifiers = instruction.getModifiers();
		Accessors accessor = instruction.getAccessor();
		Iterator<Token> iterator = instruction.getTokens().iterator();

		Token temp = new Token("");
		while (!temp.getValue().equals("(")) {
			temp = iterator.next();
		}

		int callPos = temp.getPosition();
		Map<String, String> parameterMap = InstructionUtil.getParameterList(iterator, temp);

		temp = new Token("");
		iterator = instruction.getTokens().iterator();
		while (!temp.getValue().contains("<") && iterator.hasNext() && temp.getPosition() < callPos) {
			temp = iterator.next();
		}

		Token genericToken = iterator.hasNext() && temp.getPosition() < callPos
				? InstructionUtil.getGenericHeader(iterator, temp)
				: new Token("none");

		Slice currentSlice = context.getSlice();

		if (currentSlice == null) {
			return;
		}

		String className = currentSlice.getMetaData().name();
		Method method = new Method(className, "special.constructor", accessor, genericToken.getValue());

		method.addArgument(parameterMap.values());
		method.addModifier(modifiers);

		for (String varName : parameterMap.keySet()) {
			String typeName = parameterMap.get(varName);
			context.addLocalVariable(typeName, varName);
		}

		context.setMethod(method);
		currentSlice.addMethod(method);

		context.changeScope(ScopeEnum.INSTRUCTION);
	}

}

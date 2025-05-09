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

public class GenericMethodBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		List<Modifier> modifiers = instruction.getModifiers();
		Accessors accessor = instruction.getAccessor();

		Token methodName = null;
		Token returnType = null;

		List<Token> tokens = instruction.getTokens();
		Iterator<Token> iterator = tokens.iterator();

		Token startToken = new Token("");
		while (!startToken.getValue().contains("<") && iterator.hasNext()) {
			startToken = iterator.next();
		}

		Token genericToken = InstructionUtil.getGenericHeader(iterator, startToken);

		returnType = iterator.next();
		returnType = InstructionUtil.resolveType(tokens, returnType.getPosition());

		methodName = tokens.get(returnType.getPosition() + 1);

		Token temp = new Token("");

		iterator = instruction.getTokens().iterator();
		while (iterator.hasNext() && !temp.getValue().equals("(")) {
			temp = iterator.next();
		}

		Method method = new Method(methodName.getValue(), returnType.getValue(), accessor, genericToken.getValue());
		Map<String, String> parameters = InstructionUtil.getParameterList(iterator, temp);
		method.addArgument(parameters.values());
		method.addModifier(modifiers);

		for (String varName : parameters.keySet()) {
			String typeName = parameters.get(varName);
			context.addLocalVariable(typeName, varName);
		}

		Slice currentSlice = context.getSlice();

		context.setMethod(method);
		currentSlice.addMethod(method);

		context.changeScope(ScopeEnum.INSTRUCTION);
	}

}

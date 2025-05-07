package com.aspodev.parser.Behavior;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aspodev.SCAR.Modifier;
import com.aspodev.SCAR.Slice;
import com.aspodev.SCAR.Accessors;
import com.aspodev.SCAR.Method;
import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.Instructions.Instruction;
import com.aspodev.parser.Instructions.InstructionUtil;
import com.aspodev.parser.Scope.ScopeEnum;

public class MethodBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		List<Modifier> modifiers = instruction.getModifiers();
		Accessors accessor = instruction.getAccessor();
		Token methodName = null;
		Token returnType = null;
		Token genericTypes = new Token("none");

		if (isGenericMethod(instruction)) {
			List<Token> tokens = instruction.getTokens();

			Iterator<Token> iterator = tokens.iterator();
			Token startToken = new Token("");

			while (!startToken.getValue().contains("<") && iterator.hasNext()) {
				startToken = iterator.next();
			}

			genericTypes = InstructionUtil.getGenericHeader(iterator, startToken);

			returnType = iterator.next();
			returnType = InstructionUtil.resolveType(tokens, returnType.getPosition());

			methodName = tokens.get(returnType.getPosition() + 1);
		} else {
			int returnTypeIndex = instruction.getIdentifier(0).getPosition();
			returnType = InstructionUtil.resolveType(instruction.getTokens(), returnTypeIndex);
			methodName = instruction.getToken(returnType.getPosition() + 1);
		}

		Iterator<Token> iterator = instruction.getTokens().iterator();

		Token temp = new Token("");
		while (iterator.hasNext() && !temp.getValue().equals("(")) {
			temp = iterator.next();
		}

		Method method = new Method(methodName.getValue(), returnType.getValue(), accessor, genericTypes.getValue());
		Map<String, String> parameters = InstructionUtil.getParameterList(iterator, temp);
		method.addArgument(parameters.values());
		method.addModifier(modifiers);

		Slice currentSlice = context.getSlice();

		context.setMethod(method);
		currentSlice.addMethod(method);

		context.changeScope(ScopeEnum.INSTRUCTION);
	}

	public boolean isGenericMethod(Instruction instruction) {
		Token firstId = instruction.getIdentifier(0);
		int firstIdPos = firstId.getPosition();
		if (firstIdPos == 0)
			return false;

		if (instruction.getToken(firstIdPos - 1).getValue().contains("<"))
			return true;

		return false;
	}

}

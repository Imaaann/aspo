package com.aspodev.parser.Behavior;

import java.util.List;

import com.aspodev.SCAR.Slice;
import com.aspodev.TypeParser.TypeToken;
import com.aspodev.TypeParser.TypeTokenEnum;
import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.Instructions.Instruction;
import com.aspodev.parser.Scope.ScopeEnum;

public class AnonymousBehavior implements Behavior {
	private static int anonNumber = 0;

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		anonNumber++;
		new OtherBehavior().apply(context, instruction);
		context.rewindScope();

		List<Token> tokens = instruction.getTokens();
		int newIndex = tokens.lastIndexOf(new Token("new"));

		Token constructorName = tokens.get(newIndex + 1);
		createClass(context, constructorName.getValue());
	}

	private void createClass(ParserContext context, String constructorName) {
		String className = context.getClassName();
		String anonClassName = "%s$%d".formatted(className, anonNumber);
		context.createSlice(new TypeToken(anonClassName, context.getPackage(), TypeTokenEnum.CLASS));
		Slice newSlice = context.getSlice();
		TypeToken constructorType = context.getTypeToken(constructorName);
		newSlice.setParentName(constructorType == null ? constructorName : constructorType.getFullName());
		context.changeScope(ScopeEnum.CLASS);
		context.addLocalVariable(anonClassName, "this");
		context.addLocalVariable(constructorName, "super");
	}

}

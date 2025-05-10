package com.aspodev.parser.Behavior;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.aspodev.SCAR.Accessors;
import com.aspodev.SCAR.Attribute;
import com.aspodev.SCAR.Dependency;
import com.aspodev.SCAR.Slice;
import com.aspodev.TypeParser.TypeToken;
import com.aspodev.TypeParser.TypeTokenEnum;
import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.Instructions.Instruction;
import com.aspodev.parser.Instructions.InstructionUtil;
import com.aspodev.parser.Scope.ScopeEnum;

public class EnumeratorBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		Slice enumSlice = context.getSlice();
		String enumName = enumSlice.getMetaData().name();

		List<Token> enumeratorNames = new ArrayList<>();
		List<Token> instructionTokens = new ArrayList<>();

		List<Token> list = instruction.getTokens();
		Iterator<Token> iterator = list.iterator();
		Token temp = new Token("");

		while (iterator.hasNext()) {
			temp = iterator.next();

			if (temp.isIdentifier())
				enumeratorNames.add(temp);

			if (temp.getValue().equals("(")) {
				int count = 1;
				while (count > 0) {
					instructionTokens.add(temp);
					temp = iterator.next();
					String tempValue = temp.getValue();

					if (tempValue.equals("("))
						count++;
					else if (tempValue.equals(")"))
						count--;
				}

			}

		}

		for (Token enumerator : enumeratorNames) {
			Attribute attribute = new Attribute(enumerator.getValue(), enumName, Accessors.PUBLIC);
			attribute.addModifier("static");
			attribute.addModifier("final");
			enumSlice.addAttribute(attribute);
		}

		List<Dependency> dependencies = InstructionUtil.getDependencies(instructionTokens, context);

		for (Dependency dependency : dependencies) {
			context.addDependency(dependency.getName(), dependency.getCallerType());
		}

		Token lastToken = list.get(list.size() - 1);
		if (lastToken.getValue().equals("{")) {
			Token name = enumeratorNames.get(enumeratorNames.size() - 1);
			enumeratorClassDeclaration(name.getValue(), context, enumName);
		} else if (lastToken.getValue().equals("}")) {
			new EndBlockBehavior().apply(context, instruction);
		}

	}

	private void enumeratorClassDeclaration(String name, ParserContext context, String enumName) {
		context.createSlice(new TypeToken(name, context.getPackage(), TypeTokenEnum.CLASS));
		Slice newSlice = context.getSlice();
		newSlice.setParentName(enumName);
		newSlice.addModifier("final");
		context.changeScope(ScopeEnum.CLASS);
		context.addLocalVariable(name, "this");
		context.addLocalVariable(enumName, "super");
	}

}

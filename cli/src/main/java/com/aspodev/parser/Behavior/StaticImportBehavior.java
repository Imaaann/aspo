package com.aspodev.parser.Behavior;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.Instructions.Instruction;

public class StaticImportBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		Token identifier = instruction.getIdentifier(0);
		List<String> components = Arrays.asList(identifier.getValue().split("\\."));
		int size = components.size();

		if (identifier.getValue().endsWith("*")) {
			String className = components.stream().limit(size - 1).collect(Collectors.joining("."));
			context.addStaticClass(className);
		} else {
			context.addStaticMethod(identifier.getValue());
		}

	}

}

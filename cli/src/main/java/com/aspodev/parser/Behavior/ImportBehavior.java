package com.aspodev.parser.Behavior;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.aspodev.TypeParser.TypeToken;
import com.aspodev.TypeParser.TypeTokenEnum;
import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.Instructions.Instruction;

public class ImportBehavior implements Behavior {
	public void apply(ParserContext context, Instruction instruction) {
		Token identifier = instruction.getIdentifier(0);
		List<String> components = Arrays.asList(identifier.getValue().split("\\."));
		int size = components.size();
		String typeName = components.get(size - 1);
		String pkgName = components.stream().limit(size - 1).collect(Collectors.joining("."));

		TypeToken newType = new TypeToken(typeName, pkgName, TypeTokenEnum.IMPORTED);
		context.addType(newType);

		System.out.println("[DEBUG] imported: " + newType);
	}
}

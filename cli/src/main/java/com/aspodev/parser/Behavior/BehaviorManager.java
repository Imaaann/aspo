package com.aspodev.parser.Behavior;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.aspodev.TypeParser.TypeToken;
import com.aspodev.TypeParser.TypeTokenEnum;
import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.Instructions.Instruction;
import com.aspodev.parser.Instructions.InstructionTypes;

public class BehaviorManager {
	public static BehaviorManager instance = new BehaviorManager();
	private final Map<InstructionTypes, Behavior> registry = new EnumMap<>(InstructionTypes.class);

	private BehaviorManager() {
		registry.put(InstructionTypes.IMPORT_STATEMENT, (context, instruction) -> {
			Token identifier = instruction.getIdentifier(0);
			List<String> components = Arrays.asList(identifier.getValue().split("\\."));
			int size = components.size();
			String typeName = components.get(size - 1);
			String pkgName = components.stream().limit(size - 1).collect(Collectors.joining("."));

			TypeToken newType = new TypeToken(typeName, pkgName, TypeTokenEnum.IMPORTED);
			context.addType(newType);

			System.out.println("[DEBUG] imported: " + newType);
		});

		registry.put(InstructionTypes.PACKAGE_STATEMENT, (context, instruction) -> {
			Token identifier = instruction.getIdentifier(0);
			context.addPackage(identifier.getValue());
		});

		registry.put(InstructionTypes.ATTRIBUTE_DECLARATION, null);
	}

	public static BehaviorManager getInstance() {
		return instance;
	}

	public void execute(ParserContext context, Instruction instruction) {
		registry.getOrDefault(instruction.getType(), (c, i) -> {
		}).apply(context, instruction);
	}

}

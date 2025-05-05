package com.aspodev.parser.Behavior;

import java.util.EnumMap;
import java.util.Map;

import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Instructions.Instruction;
import com.aspodev.parser.Instructions.InstructionTypes;

public class BehaviorManager {
	public static BehaviorManager instance = new BehaviorManager();
	private final Map<InstructionTypes, Behavior> registry = new EnumMap<>(InstructionTypes.class);

	private BehaviorManager() {
		registry.put(InstructionTypes.IMPORT_STATEMENT, new ImportBehavior());
		registry.put(InstructionTypes.PACKAGE_STATEMENT, new PackageBehavior());
		registry.put(InstructionTypes.CLASS_DECLARATION, new ClassBehavior());
	}

	public static BehaviorManager getInstance() {
		return instance;
	}

	public void execute(ParserContext context, Instruction instruction) {
		registry.getOrDefault(instruction.getType(), (c, i) -> {
			System.out.println("[DEBUG] == unhandled instruction: " + instruction);
		}).apply(context, instruction);
	}

}

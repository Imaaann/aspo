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
		registry.put(InstructionTypes.INTERFACE_DECLARATION, new InterfaceBehavior());
		registry.put(InstructionTypes.RECORD_DECLARATION, new RecordBehavior());
		registry.put(InstructionTypes.ENUM_DECLARTION, new EnumBehavior());
	}

	public static BehaviorManager getInstance() {
		return instance;
	}

	public void execute(ParserContext context, Instruction instruction) {
		registry.getOrDefault(instruction.getType(), (c, i) -> {
			if (i.getType() == InstructionTypes.OTHER)
				return;

			System.out.println("[DEBUG] == unhandled instruction: " + instruction);
		}).apply(context, instruction);
	}

}

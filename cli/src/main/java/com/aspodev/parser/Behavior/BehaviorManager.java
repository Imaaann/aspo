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
		registry.put(InstructionTypes.END_OF_BLOCK, new EndBlockBehavior());
		registry.put(InstructionTypes.ATTRIBUTE_DECLARATION, new AttributeBehavior());
		registry.put(InstructionTypes.METHOD_DECLARATION, new MethodBehavior());
		registry.put(InstructionTypes.OTHER, new OtherBehavior());
		registry.put(InstructionTypes.LOCAL_VARIABLE_DECLARATION, new LocalVariableBhavior());
		registry.put(InstructionTypes.GENERIC_METHOD_DECLARATION, new GenericMethodBehavior());
		registry.put(InstructionTypes.CONSTRUCTOR_DEFENITION, new ConstructorBehavior());
		registry.put(InstructionTypes.STATIC_INITIALZATION, new StaticInitBehavior());
		registry.put(InstructionTypes.INITIALAZATION_BLOCK, new InitializerBehavior());
		registry.put(InstructionTypes.ENUMERATOR_DECLARATION, new EnumeratorBehavior());
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

package com.aspodev.parser.Behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.Instructions.Instruction;
import com.aspodev.parser.Instructions.InstructionClassifier;

public class LabelCaseBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		List<Token> tokens = instruction.getTokens();
		ListIterator<Token> iterator = tokens.listIterator(0);

		List<Token> element = new ArrayList<>();
		Token temp = new Token("");

		while (iterator.hasNext() && !temp.getValue().equals("->")) {
			temp = iterator.next();

			String value = temp.getValue();
			if (!value.equals(",") && !value.equals("case") && !value.equals("->")) {
				element.add(temp);
			} else {

				String typeName;
				String varName;
				if (element.size() == 2) {
					typeName = element.get(0).getValue();
					varName = element.get(1).getValue();
					context.addLocalVariable(typeName, varName);
				}

				element.clear();
			}

		}

		// Execute the remaining tokens as a seperate instruction
		List<String> remainingTokens = new ArrayList<>();
		iterator.forEachRemaining(t -> remainingTokens.add(t.getValue()));
		InstructionClassifier classifier = new InstructionClassifier(remainingTokens);
		Instruction subInstruction = classifier.classify(context);
		BehaviorManager.getInstance().execute(context, subInstruction);

	}

}

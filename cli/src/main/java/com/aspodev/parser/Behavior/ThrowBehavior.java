package com.aspodev.parser.Behavior;

import com.aspodev.SCAR.Slice;
import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.Instructions.Instruction;

public class ThrowBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		Token exceptionId = new Token("");

		boolean passedThrow = false;
		for (Token token : instruction.getTokens()) {
			if (token.isIdentifier() && passedThrow) {
				exceptionId = token;
				break;
			}

			if (token.getValue().equals("throw"))
				passedThrow = true;
		}

		Slice currentSlice = context.getSlice();

		if (currentSlice == null) {
			return;
		}

		if (instruction.getToken(exceptionId.getPosition() - 1).equals(new Token("new"))) {
			currentSlice.addException(exceptionId.getValue());
		} else {
			String exceptionName = context.getVariableType(exceptionId.getValue());

			if (exceptionName == null)
				currentSlice.addException("UNKOWN");
			else
				currentSlice.addException(exceptionName);

		}

		new OtherBehavior().apply(context, instruction);

	}

}

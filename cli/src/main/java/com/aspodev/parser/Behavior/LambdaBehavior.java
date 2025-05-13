package com.aspodev.parser.Behavior;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.Instructions.Instruction;
import com.aspodev.parser.Scope.ScopeEnum;

public class LambdaBehavior implements Behavior {

	@Override
	public void apply(ParserContext context, Instruction instruction) {
		Token arrowToken = instruction.getToken("->");
		Token temp = new Token("");

		List<Token> tokens = instruction.getTokens();
		ListIterator<Token> iterator = tokens.listIterator(arrowToken.getPosition());
		List<Token> argumentList = new ArrayList<>();
		boolean insideArgumentList = false;

		while (iterator.hasPrevious()) {
			temp = iterator.previous();

			if (!insideArgumentList && temp.isIdentifier()) {
				argumentList.add(temp);
				break;
			}

			if (temp.getValue().equals(")")) {
				insideArgumentList = true;
			}

			if (temp.getValue().equals("(")) {
				insideArgumentList = false;
				argumentList.add(0, temp);
				break;
			}

			if (insideArgumentList) {
				argumentList.add(0, temp);
			}

		}

		List<Token> params = argumentList.stream().filter(t -> t.isIdentifier()).toList();
		int count = 0;
		for (Token p : params) {
			String typeName = "LAMBDA$%d".formatted(count);
			count++;
			context.addLocalVariable(typeName, p.getValue());
		}

		if (context.getCurrentScope() == ScopeEnum.INSTRUCTION
				|| context.getCurrentScope() == ScopeEnum.SWITCH_STATEMENT) {
			new OtherBehavior().apply(context, instruction);
		}

	}

}

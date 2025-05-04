package com.aspodev.parser.Instructions;

import java.util.Iterator;
import java.util.List;

import com.aspodev.parser.Token;
import com.aspodev.parser.TokenTypes;

public class InstructionUtil {
    public static Token getGenericHeader(Iterator<Token> iterator, Token startToken) {
        Token temp;
        int sign = 0;
        int counter = startToken.getValue().length();
        int length;
        Token token = startToken;
        do {
            temp = iterator.next();
            length = temp.getValue().length();
            sign = 0;

            if (temp.getValue().contains(">")) {
                sign = -1;
            } else if (temp.getValue().contains("<")) {
                sign = 1;
            }

            counter = counter + sign * length;
            token.append(temp);
        } while (!(counter == 0) && iterator.hasNext());
        return token;

    }
}

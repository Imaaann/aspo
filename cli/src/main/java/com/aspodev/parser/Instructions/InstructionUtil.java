package com.aspodev.parser.Instructions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.aspodev.SCAR.Modifier;
import com.aspodev.parser.Token;

public class InstructionUtil {

    public static Token getGenericHeader(Iterator<Token> iterator, Token startToken) {
        Token temp;
        int sign = 0;
        int counter = startToken.getValue().length();
        int length;
        Token token = new Token(startToken.getValue());
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

    public static Map<String, String> getParameterList(Iterator<Token> iterator, Token startToken) {
        Token token1, token2;
        Map<String, String> ParmaterMap = new HashMap<>();
        token1 = iterator.next();
        while (!(token1.getValue().equals(")")) && iterator.hasNext()) {
            if (token1.getValue().equals(","))
                token1 = iterator.next();
            token2 = iterator.next();
            if (token2.getValue().contains("<")) {
                token1.append(getGenericHeader(iterator, token2));
                token2 = iterator.next();
            }
            ParmaterMap.put(token2.getValue(), token1.getValue());
            token1 = iterator.next();
        }

        return ParmaterMap;

    }

    public static int getClassNamePosition(List<Modifier> modifiers) {
        if (modifiers.contains(Modifier.NON_SEALED))
            return 2;

        if (modifiers.contains(Modifier.SEALED))
            return 1;

        return 0;
    }

    public static List<Token> getCommaSeperatedList(Instruction instruction, String beginToken) {
        Token begin = instruction.getToken(beginToken);

        if (begin == null)
            return null;

        List<Token> list = new ArrayList<>();

        int position = begin.getPosition();
        Token commaToken;

        do {
            Token interfaceName = instruction.getToken(position + 1);
            commaToken = instruction.getToken(position + 2);
            position = commaToken.getPosition();
            list.add(interfaceName);
        } while (commaToken.getValue().equals(","));

        return list;
    }
}

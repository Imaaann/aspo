package com.aspodev.parser.Instructions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.aspodev.SCAR.Dependency;
import com.aspodev.SCAR.Modifier;
import com.aspodev.parser.ParserContext;
import com.aspodev.parser.Token;
import com.aspodev.parser.TokenTypes;

public class InstructionUtil {

    public static Token resolveType(List<Token> tokens, int typePos) {
        Token type = tokens.get(typePos);
        Token temp = tokens.get(typePos + 1);

        if (temp.getValue().contains("<")) {

            Iterator<Token> iterator = tokens.iterator();
            temp = new Token("");

            while (!temp.getValue().contains("<") && iterator.hasNext()) {
                temp = iterator.next();
            }

            Token genericHeader = InstructionUtil.getGenericHeader(iterator, temp);

            if (iterator.hasNext()) {
                type.append(genericHeader);
                type.setPosition(genericHeader.getPosition());
            }
        }

        if (temp.getValue().equals("[")) {
            type.append(new Token("[]"));
            type.setPosition(typePos + 2);
        }

        if (temp.getValue().equals("...")) {
            type.append(new Token("..."));
            type.setPosition(typePos + 1);
        }

        return type;
    }

    public static int resolveTypeLength(List<Token> tokens, int typePos) {
        Token temp = tokens.get(typePos + 1);
        int finalPos = typePos;

        if (temp.getValue().contains("<")) {

            Iterator<Token> iterator = tokens.iterator();
            temp = new Token("");

            while (!temp.getValue().contains("<") && iterator.hasNext()) {
                temp = iterator.next();
            }

            Token genericHeader = InstructionUtil.getGenericHeader(iterator, temp);

            if (iterator.hasNext()) {
                finalPos = genericHeader.getPosition();
            }
        }

        if (temp.getValue().equals("[")) {
            finalPos = typePos + 2;
        }

        if (temp.getValue().equals("...")) {
            finalPos = typePos + 1;
        }

        return finalPos;
    }

    public static Token getGenericHeader(Iterator<Token> iterator, Token startToken) {
        Token temp;
        int sign = 0;
        int pos = startToken.getPosition();
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

            if (counter == 0) {
                pos = temp.getPosition();
            }

        } while (!(counter == 0) && iterator.hasNext());

        token.setPosition(pos);
        return token;

    }

    public static Map<String, String> getParameterList(Iterator<Token> iterator, Token startToken) {
        Token token1, token2;
        Map<String, String> ParmaterMap = new LinkedHashMap<>();
        token1 = iterator.next();
        while (!(token1.getValue().equals(")")) && iterator.hasNext()) {
            if (token1.getValue().equals(","))
                token1 = iterator.next();
            token2 = iterator.next();
            if (token2.getValue().contains("<")) {
                token1.append(getGenericHeader(iterator, token2));
                token2 = iterator.next();
            }

            if (token2.getValue().equals("[")) {
                token1.append(new Token("[]"));
                iterator.next();
                token2 = iterator.next();
            }

            if (token2.getValue().equals("...")) {
                token1.append(token2);
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
            Token name = instruction.getToken(position + 1);
            commaToken = instruction.getToken(position + 2);
            position = commaToken.getPosition();
            list.add(name);
        } while (commaToken.getValue().equals(","));

        return list;
    }

    public static List<Token> getCommaSeperatedList(Instruction instruction, int firstElementPos) {
        Token element;
        Token commaToken;
        int pos = firstElementPos;
        List<Token> list = new ArrayList<>();

        do {
            element = instruction.getToken(pos);
            commaToken = instruction.getToken(pos + 1);
            pos = commaToken.getPosition() + 1;
            list.add(element);
        } while (commaToken.getValue().equals(","));

        return list;
    }

    public static List<Dependency> getDependencies(List<Token> tokens, ParserContext context) {
        List<Token> idfs = tokens.stream().filter(t -> {
            if (!t.isIdentifier())
                return false;

            boolean chainIdf = t.getType() == TokenTypes.CHAINED_IDENTIFIER && !t.getValue().startsWith(".");

            if (chainIdf)
                return true;

            int finalPos = InstructionUtil.resolveTypeLength(tokens, t.getPosition());
            Token nextToken = tokens.get(finalPos + 1);
            return nextToken != null && nextToken.getValue().equals("(");

        }).toList();

        List<Dependency> dependencies = new ArrayList<>();

        for (Token id : idfs) {
            if (id.getType() == TokenTypes.CHAINED_IDENTIFIER) {
                String[] components = id.getValue().split("\\.");
                String varType = context.getVariableType(components[0]);

                if (context.isType(new Token(components[0]))) {
                    varType = components[0];
                }

                if (varType == null) {
                    varType = "UNKOWN";
                    System.out.println("[WARN] Unkown dependency: " + Arrays.asList(components));
                }

                dependencies.add(new Dependency(components[1], varType));
            } else {
                String callerType = "RESOLVE";

                if (context.isType(id)) {
                    callerType = id.getValue() + ".static";
                }

                if (tokens.get(id.getPosition() - 1).equals(new Token("new"))) {
                    callerType = id.getValue() + ".construct";
                }

                dependencies.add(new Dependency(id.getValue(), callerType));
            }
        }

        return dependencies;
    }

    public static String resolveLiteral(Token token) {
        String value = token.getValue();
        if (value.equals("null"))
            return "null";
        if (value.equals("true") || value.equals("false"))
            return "boolean";
        if (token.getType() == TokenTypes.LITERAL) {
            if (value.contains(".") || value.contains("p"))
                return "double";
            return "int";
        }

        return "String";
    }

    public static String getChainedElementsExcept(String element, int amount) {
        List<String> components = Arrays.asList(element.split("\\."));
        int size = components.size();
        return components.stream().limit(size - amount).collect(Collectors.joining("."));
    }

    public static String getChainedElementsExcept(String element) {
        return InstructionUtil.getChainedElementsExcept(element, 1);
    }
}

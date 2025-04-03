package com.aspodev.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTools {
    public static void removePattern(StringBuilder stringBuilder, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(stringBuilder);

        StringBuilder temp = new StringBuilder();
        int lastEnd = 0;

        while (matcher.find()) {
            temp.append(stringBuilder, lastEnd, matcher.start());
            lastEnd = matcher.end();
        }
        temp.append(stringBuilder, lastEnd, stringBuilder.length());

        stringBuilder.setLength(0);
        stringBuilder.append(temp);
    }    
}

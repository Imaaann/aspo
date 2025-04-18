package com.aspodev.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTools {
    public static void removePattern(StringBuilder str, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(str);

        StringBuilder temp = new StringBuilder();
        int lastEnd = 0;

        while (matcher.find()) {
            temp.append(str, lastEnd, matcher.start());
            lastEnd = matcher.end();
        }
        temp.append(str, lastEnd, str.length());

        str.setLength(0);
        str.append(temp);
    }

    public static List<String> splitAround(String str, String regex) {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(str);

        int lastEnd = 0;
        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                result.add(str.substring(lastEnd, matcher.start()));
            }

            result.add(matcher.group());
            lastEnd = matcher.end();
        }

        if (lastEnd < str.length()) {
            result.add(str.substring(lastEnd));
        }

        return result;
    }

    public static List<String> splitAcross(String str, String regex) {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(str);

        int lastEnd = 0;
        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                result.add(str.substring(lastEnd, matcher.start()));
            }
            lastEnd = matcher.end();
        }

        if (lastEnd < str.length()) {
            result.add(str.substring(lastEnd));
        }

        return result;
    }

    public static boolean stringContainsRegex(String str, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }
}

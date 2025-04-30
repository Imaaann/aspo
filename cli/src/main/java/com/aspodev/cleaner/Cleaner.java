package com.aspodev.cleaner;

import com.aspodev.utils.RegexTools;

public class Cleaner {

    /**
     * @param contents the contents to the file that will be cleaned
     */
    public static void cleanFile(StringBuilder contents) {
        cleanComments(contents);
        cleanAnnotations(contents);
        cleanStrings(contents);
    }

    private static void cleanStrings(StringBuilder contents) {
        String singleLineString = "\"((?:\\\\.|[^\"\\\\])*)\"";
        String multiLineString = "\"\"\"((?:\\\\.|[^\\\\\"]|\"(?!\")|\"\"(?!\"))*?)\"\"\"";

        RegexTools.removePattern(contents, multiLineString);
        RegexTools.removePattern(contents, singleLineString);
    }

    private static void cleanComments(StringBuilder contents) {

        String multiLineComment = "/\\*.*?\\*/";
        String singleLineComment = "^(?<!\")\\s+//.*?$";

        RegexTools.removePattern(contents, singleLineComment);
        RegexTools.removePattern(contents, multiLineComment);
    }

    private static void cleanAnnotations(StringBuilder contents) {
        String annotations = "@\\w+\\s*(\\(.*?\\))?";

        RegexTools.removePattern(contents, annotations);
    }

}

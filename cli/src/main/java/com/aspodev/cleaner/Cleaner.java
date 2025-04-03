package com.aspodev.cleaner;

import com.aspodev.utils.RegexTools;

public class Cleaner {
   
    /**
     * @param contents the contents to the file that will be cleaned
     */
    public static void cleanFile(StringBuilder contents) {
        cleanComments(contents);
        cleanAnnotations(contents);
    }

    private static void cleanComments(StringBuilder contents) {
        String singleLineComment = "//.*?$";
        String multiLineComment = "/\\*.*?\\*/";

        RegexTools.removePattern(contents, singleLineComment);
        RegexTools.removePattern(contents, multiLineComment);
    }

    private static void cleanAnnotations(StringBuilder contents) {
        String annotations = "@\\w+\\s*(\\(.*?\\))?";

        RegexTools.removePattern(contents, annotations);
    }  

}

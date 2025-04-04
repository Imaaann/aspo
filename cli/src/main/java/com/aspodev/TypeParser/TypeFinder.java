package com.aspodev.TypeParser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypeFinder {

    public static List<TypeToken> findAllTypes(StringBuilder contents) {
        String pkg  = findPackage(contents);
        ArrayList<TypeToken> result = new ArrayList<>();

        result.addAll(findClasses(contents, pkg));
        result.addAll(findInterfaces(contents, pkg));
        result.addAll(findEnums(contents, pkg));
        result.addAll(findRecords(contents, pkg));

        return result;
    }

    private static String findPackage(StringBuilder contents) {
        String packageRegex = "package\\s+([\\w|\\.]+)";

        Pattern pattern = Pattern.compile(packageRegex, Pattern.MULTILINE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(contents);

        return matcher.find() ? matcher.group(1) : "NONE";
    }

    private static List<TypeToken> findType(StringBuilder contents, String pkg, String regex, TypeTokenEnum type) {
        ArrayList<TypeToken> result = new ArrayList<TypeToken>();


        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(contents);

        while (matcher.find()) {
            String className = matcher.group(1);
            TypeToken classToken = new TypeToken(className, pkg, type);
            result.add(classToken);
        }

        return result;
    }

    private static List<TypeToken> findClasses(StringBuilder contents, String pkg) {
        return findType(contents, pkg, "class\\s+(\\w+)", TypeTokenEnum.CLASS);
    } 

    private static List<TypeToken> findInterfaces(StringBuilder contents, String pkg) {
        return findType(contents, pkg, "interface\\s+(\\w+)", TypeTokenEnum.INTERFACE);
    }  

    private static List<TypeToken> findEnums(StringBuilder contents, String pkg) {
        return findType(contents, pkg, "enum\\s+(\\w+)", TypeTokenEnum.ENUM);
    }  

    private static List<TypeToken> findRecords(StringBuilder contents, String pkg) {
        return findType(contents, pkg, "record\\s+(\\w+)", TypeTokenEnum.RECORD);
    }  
}
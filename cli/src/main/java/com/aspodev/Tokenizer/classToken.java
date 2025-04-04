package main.java.com.aspodev.cli.Tokenizer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ClassesToken {

    private List<String> classes;

    public ClassesToken() {
        classes = new ArrayList<>();
    }

    public List<String> getClasses() {
        return classes;
    }

    public void regexTokens(String filePath) throws IOException {

        File file = new File(filePath);
        if (!file.isFile() || !file.exists()) {
            throw new IOException("File not found: " + filePath);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            StringBuilder fileContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }

            // Regular expression to find class names only
            String exRegex = "class\\s+(\\w+)";

            Pattern pattern = Pattern.compile(exRegex);
            Matcher matcher = pattern.matcher(fileContent);

            while (matcher.find()) {
                String className = matcher.group(1); // Extract only the class name
                classes.add(className);
            }
        }
    }
}

class InterfaceToken {

    private List<String> interfaces;

    public InterfaceToken() {
        interfaces = new ArrayList<>();
    }

    public List<String> getInterfaces() {
        return interfaces;
    }

    public void regexTokens(String filePath) throws IOException {

        File file = new File(filePath);
        if (!file.isFile() || !file.exists()) {
            throw new IOException("File not found: " + filePath);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            StringBuilder fileContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }

            // Regular expression to find interface names only
            String methodRegex = "interface\\s+(\\w+)";

            Pattern pattern = Pattern.compile(methodRegex);
            Matcher matcher = pattern.matcher(fileContent);
            while (matcher.find()) {
                String interfaceName = matcher.group(1); // Extract only the interface name
                interfaces.add(interfaceName);
            }
        }
    }
}

class EnumsToken {

    private List<String> enums;

    public EnumsToken() {
        enums = new ArrayList<>();
    }

    public List<String> getEnums() {
        return enums;
    }

    public void regexTokens(String filePath) throws IOException {

        File file = new File(filePath);
        if (!file.isFile() || !file.exists()) {
            throw new IOException("File not found: " + filePath);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            StringBuilder fileContent = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }

            // Regular expression to find enum names only
            String methodRegex = "enum\\s+(\\w+)";

            Pattern pattern = Pattern.compile(methodRegex);
            Matcher matcher = pattern.matcher(fileContent);
            while (matcher.find()) {
                String enumName = matcher.group(1); // Extract only the enum name
                enums.add(enumName);
            }
        }
    }
}

// public class classToken {

//     public static void main(String[] args) {
//         ClassesToken classesToken = new ClassesToken();
//         InterfaceToken interfaceToken = new InterfaceToken();
//         EnumsToken enumsToken = new EnumsToken();

//         String filePath = "C:\\Users\\Imad\\Desktop\\Projet\\hello.java";

//         try {
//             classesToken.regexTokens(filePath);
//             interfaceToken.regexTokens(filePath);
//             enumsToken.regexTokens(filePath);

//             System.out.println("Classes: " + classesToken.getClasses());
//             System.out.println("Interfaces: " + interfaceToken.getInterfaces());
//             System.out.println("Enums: " + enumsToken.getEnums());

//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }
// }
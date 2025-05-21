package com.aspodev.web.Service;

import com.aspodev.utils.JsonRead;
import com.aspodev.web.Model.Command;
import com.aspodev.web.Model.CommandResult;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class JsonReading extends JsonRead {
    public static Command readCommand(String filePath) {
        JSONParser parser = new JSONParser();
        try(FileReader jsonfile = new FileReader(filePath)){
            JSONObject jsonObject = (JSONObject) parser.parse(jsonfile);
            String command_S = (String) jsonObject.get("command");
            return new Command(command_S);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeOut(String filePath, CommandResult command) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("command", command.getCommand());
        jsonObject.put("stdout", command.getStdout());
        jsonObject.put("stderr", command.getStderr());

        try (FileWriter file = new FileWriter(filePath)) {
            file.write(jsonObject.toJSONString());
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readOut(String filePath) {
        JSONParser parser = new JSONParser();
        try(FileReader jsonfile = new FileReader(filePath)){
            JSONObject jsonObject = (JSONObject) parser.parse(jsonfile);
            String command_S = (String) jsonObject.get("command");
            String stdout = (String) jsonObject.get("stdout");
            String stderr = (String) jsonObject.get("stderr");
            return """
                    {
                    "command": %s,\n
                    "stdout": %s,\n
                    "stderr": %s
                    }
                    """.formatted(command_S, stdout, stderr);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


//    public static void main(String[] args) {
//        System.out.println(readCommand("C:\\Users\\Imad\\Desktop\\New folder (2)\\aspo\\web\\src\\resources\\data\\command.json"));
//    }
}

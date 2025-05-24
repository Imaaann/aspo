package com.web.aspoapp.Util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.web.aspoapp.Model.Command;
import com.web.aspoapp.Model.CommandResult;


public class Json {

    public static Command readCommand(String filePath) throws IOException {
        JSONParser parser = new JSONParser();
        try(FileReader jsonfile = new FileReader(filePath)){
            JSONObject jsonObject = (JSONObject) parser.parse(jsonfile);
            String commandString = (String) jsonObject.get("command");
            Command command = new Command(commandString);
            return command;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeCommand(String filePath, Command command) throws IOException {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("command", command.getCommand());
    try (FileWriter fileWriter = new FileWriter(filePath)) {
        jsonObject.writeJSONString(fileWriter);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
}

    public static void writeOutput(String filePath, CommandResult commandResult) throws IOException {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("stdout", commandResult.getStdout());
    jsonObject.put("stderr", commandResult.getStderr());
    jsonObject.put("exitCode", commandResult.getExitCode());
    try (FileWriter fileWriter = new FileWriter(filePath)) {
        jsonObject.writeJSONString(fileWriter);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
}

    public static CommandResult readOutput(String filePath) throws IOException {
        JSONParser parser = new JSONParser();
        try(FileReader jsonfile = new FileReader(filePath)){
            JSONObject jsonObject = (JSONObject) parser.parse(jsonfile);
            String stdoutString = (String) jsonObject.get("stdout");
            String stderrString = (String) jsonObject.get("stderr");
            int exitCode = ((Long) jsonObject.get("exitCode")).intValue();
            CommandResult commandResult = new CommandResult();
            commandResult.setStdout(stdoutString);
            commandResult.setStderr(stderrString);
            commandResult.setExitCode(exitCode);

            return commandResult;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static void main(String[] args) throws IOException {
////        Command command = new Command("ls -l");
////        //CommandResult commandResult = new CommandResult(command, "stdout", "stderr", 0);
////        writeCommand("C:\\Users\\Imad\\Desktop\\New folder (2)\\Latest\\aspoapp\\src\\main\\resources\\command.json", command);
//        //writeOutput("resources/output.json", commandResult);
//        System.out.println(readCommand("C:\\Users\\Imad\\Desktop\\New folder (2)\\Latest\\aspoapp\\src\\main\\resources\\command.json").getCommand());
//    }
}
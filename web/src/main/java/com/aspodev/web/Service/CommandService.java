package com.aspodev.web.Service;

import com.aspodev.web.Exception.CommandException;
import com.aspodev.web.Model.Command;
import com.aspodev.web.Model.CommandResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommandService {
    @Autowired
    private static Command command_docker;
    private static CommandResult Output;
    private static File CWD;
    private final String container_name = "as aspo container";

    public CommandService() {
        Output = new CommandResult();
        CWD = new File(System.getProperty("user.dir"));
        command_docker = new Command(String.format("docker exec %s /bin/bash -c ", container_name));
    }

    public CommandResult getOutput() {
        return Output;
    }

    public static File getCWD() {
        return CWD;
    }

    public void executeCommand(Command command , String C_name) throws InterruptedException {
        String cmd = command_docker.getCommand() + "\"" + command + "\"";
        int exitCode = 0;

        String path = cmd.trim().substring(3).trim();
        File newDirectory = new File(CWD, path);

        if (newDirectory.exists() && newDirectory.isDirectory()) {
            CWD = newDirectory.getAbsoluteFile();
        } else {
            Output.setStderr("Failed in finding Directory: "+path+"\n");
        }
        //other cmd
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            StringBuilder output = new StringBuilder();
            StringBuilder err_out = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );
            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream())
            );
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            while ((line = errorReader.readLine()) != null) {
                err_out.append(line);
            }

            Output.setStdout(output.toString());
            Output.setStderr(err_out.toString());

        } catch (IOException | CommandException e) {
            e.printStackTrace();
        }
    }

    // for trying outside the container
    public void executeCommand(Command command) throws InterruptedException {
        // like get the tipped command
        int exitCode;
        try {

            List<String> commandParts = new ArrayList<>();// if we use linux
            commandParts.add("/bin/sh");
            commandParts.add("-c");
            commandParts.add(command.getCommand());

            String path = command.getCommand().trim().substring(3).trim();
            File newDirectory = new File(CWD, path);
            if (newDirectory.exists() && newDirectory.isDirectory()) {
                CWD = newDirectory.getAbsoluteFile();
            } else {
                Output.setStderr("Failed in finding Directory: "+path+"\n");
            }


            ProcessBuilder processBuilder = new ProcessBuilder(commandParts);
            processBuilder.directory(CWD);
            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();
            StringBuilder err_out = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );
            BufferedReader errorReader = new BufferedReader(
                    new InputStreamReader(process.getErrorStream())
            );
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }

            while ((line = errorReader.readLine()) != null) {
                err_out.append(line);
            }

            Output.setStdout(output.toString());
            Output.setStderr(err_out.toString());
            exitCode = process.waitFor();

            if(exitCode != 0){
                Output.setStderr("command executed with : "+exitCode+output.toString());
            }
            JsonReading.writeOut("\\aspo\\web\\src\\resources\\data\\OutPut.json", Output);
            reader.close();

        } catch (IOException e) {
            Output.setStderr(e.getMessage());
            Output.setExitCode(-1);
            JsonReading.writeOut("\\aspo\\web\\src\\resources\\data\\OutPut.json", Output);
        }
    }

}
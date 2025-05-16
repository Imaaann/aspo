package com.aspodev.web.Service;

import com.aspodev.web.Exception.HostingException;
import com.aspodev.web.Model.Command;
import com.aspodev.web.Model.CommandResult;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class CommandService {
    @Autowired
    private static Command command_docker;
    private static CommandResult Output;
    private final String container_name = "as aspo container";

    public CommandService() {
        Output = new CommandResult();
        command_docker = new Command(String.format("docker exec %s /bin/bash -c ", container_name));
    }

    public CommandResult getOutput() {
        return Output;
    }

    public CommandResult executeCommand(Command command) throws InterruptedException {
        String cmd = command_docker.getCommand() + "\"" + command + "\"";
        int exitCode = 0;
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

            return Output;

        } catch (IOException | HostingException e) {
            e.printStackTrace();
            return new CommandResult(command, "", e.getMessage(), -1);
        }
    }

    // for trying outside the container
    public CommandResult executeCommand() throws InterruptedException {
        String command = String.valueOf(JsonRead.readCommand("C:\\Users\\Imad\\Desktop\\New folder (2)\\aspo\\web\\src\\resources\\data\\command.json"));
        int exitCode = 0;
        try {
            Process process = Runtime.getRuntime().exec(command);
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
            JsonRead.writeOut("C:\\Users\\Imad\\Desktop\\New folder (2)\\aspo\\web\\src\\resources\\data\\OutPut.json", Output);

            return Output;

        } catch (IOException | HostingException e) {
            e.printStackTrace();
            Command c_cmd = new Command(command);
            JsonRead.writeOut("C:\\Users\\Imad\\Desktop\\New folder (2)\\aspo\\web\\src\\resources\\data\\OutPut.json", Output);
            return new CommandResult(c_cmd, "", e.getMessage(), -1);
        }



    }

}
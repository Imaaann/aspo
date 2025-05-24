package com.web.aspoapp.Service;

import org.springframework.stereotype.Service;

import com.web.aspoapp.Model.Command;
import com.web.aspoapp.Model.CommandResult;
import com.web.aspoapp.Util.CommandExecutor;
import com.web.aspoapp.Util.Json;
import java.io.File;
import java.io.IOException;

@Service
public class CommandService {

    private File currentWorkingDirectory;
    private CommandExecutor commandResult;

    public CommandService() {
        this.currentWorkingDirectory = new File(System.getProperty("user.dir"));
        System.out.println("CommandService initialized. Current working directory on Host: " + currentWorkingDirectory.getAbsolutePath());
    }

    public CommandExecutor getCommandResult() {
        return commandResult;
    }

    public void executeCommandFromFile(String inputFilePath, String outputFilePath) {
        try {
            Command cmd = Json.readCommand(inputFilePath);
            CommandResult result = new CommandResult();
            commandResult.executeCommand(cmd);
            Json.writeOutput(outputFilePath, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Executes a command and updates the current working directory if it's a 'cd' command.
     * @param command The command to execute.
     * @return The result of the command execution.
     */
    public CommandResult executeCommand(Command command) {

        CommandResult result = commandResult.executeCommand(command.getCommand(), currentWorkingDirectory);

        if (command.getCommand().trim().startsWith("cd ") && result.getExitCode() == 0) {
            String newPath = result.getStdout().trim();
            if (!newPath.isEmpty()) {
                File newCwd = new File(newPath);
                if (newCwd.exists() && newCwd.isDirectory()) {
                    this.currentWorkingDirectory = newCwd;
                    System.out.println("Updated CWD to: " + this.currentWorkingDirectory.getAbsolutePath());
                    result.setStdout(this.currentWorkingDirectory.getAbsolutePath());
                    result.setStderr("");
                } else {
                    result.setStderr("Error:can not moviing to : " + newPath + " (invalid path or not exist)\n");
                    result.setExitCode(1);
                    result.setStdout("");
                }
            } else {
                result.setStderr("Error: 'cd'.\n");
                result.setExitCode(1);
                result.setStdout("");
            }
        }
        return result;
    }

    /**
     * Gets the current working directory path.
     * @return The absolute path of the current working directory.
     */
    public String getCurrentWorkingDirectoryPath() {
        return this.currentWorkingDirectory.getAbsolutePath();
    }
}
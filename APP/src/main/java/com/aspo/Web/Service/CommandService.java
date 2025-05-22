package com.aspo.Web.Service; // Ensure this package name matches your project structure

import org.springframework.stereotype.Service;

import com.aspo.Web.Model.Command;
import com.aspo.Web.Model.CommandResult;
import com.aspo.Web.Util.JsonReading;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommandService {

    private File currentWorkingDirectory;
    private CommandResult commandResult;

    public CommandService() {
        this.currentWorkingDirectory = new File(System.getProperty("user.dir"));
        System.out.println("CommandService initialized. Current working directory on Host: " + currentWorkingDirectory.getAbsolutePath());
    }

    public CommandResult getCommandResult() {
        return commandResult;
    }

    public void executeCommand(Command commandInput) {
        String cmd = commandInput.getCommand().trim();
        CommandResult result = new CommandResult(); 
        if (cmd.isEmpty()) {
            result.setStderr("La commande est nulle ou vide.");
            result.setExitCode(-1);
            commandResult = result;
        }

        if (cmd.toLowerCase().startsWith("cd ")) { 
            String path = cmd.substring(3).trim();
            File newDirectory = new File(currentWorkingDirectory, path);

            try {
                File absoluteNewDirectory = newDirectory.getCanonicalFile();

                if (absoluteNewDirectory.exists() && absoluteNewDirectory.isDirectory()) {
                    this.currentWorkingDirectory = absoluteNewDirectory;
                    result.setStdout(this.currentWorkingDirectory.getAbsolutePath());
                    result.setExitCode(0);
                } else {
                    result.setStderr("Impossible de trouver le répertoire ou ce n'est pas un répertoire: " + path);
                    result.setExitCode(1);
                }
            } catch (IOException e) {
                result.setStderr("Erreur lors de la résolution du chemin du répertoire: " + e.getMessage());
                result.setExitCode(1);
            }
            commandResult = result;
        }
        List<String> commandParts = new ArrayList<>();
        
            commandParts.add("cmd.exe");
            commandParts.add("/c"); 
            commandParts.add(cmd);
            
            /*
             * commandParts.add("bash");
             * commandParts.add("-c");
             * commandParts.add(cmd);
             * 
             */

        System.out.println("Executing Host command: " + String.join(" ", commandParts));

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(commandParts);
            // Set the working directory for the command on the host machine
            processBuilder.directory(this.currentWorkingDirectory);
            processBuilder.redirectErrorStream(true); // Redirect error stream to stdout for easier capture

            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();

            int exitCode = process.waitFor();

            result.setStdout(output.toString());
            result.setExitCode(exitCode);
            if (exitCode != 0) {
                result.setStderr(output.toString());
            }

        } catch (IOException e) {
            result.setStderr("Erreur d'E/S lors de l'exécution de la commande: " + e.getMessage());
            result.setExitCode(-1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            result.setStderr("Le processus a été interrompu: " + e.getMessage());
            result.setExitCode(-2);
        }
        commandResult = result;
    }

    public void executeCommandFromFile(String inputFilePath, String outputFilePath) {
        try {
            Command command = JsonReading.readCommand(inputFilePath);
            CommandResult result = new CommandResult();
            JsonReading.writeOutput(outputFilePath, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
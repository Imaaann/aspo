package com.web.aspoapp.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.web.aspoapp.Model.Command;
import com.web.aspoapp.Model.CommandResult;

public class CommandExecutor {
    private CommandResult commandResult;
    private File currentWorkingDirectory; 

    public CommandExecutor() {
        commandResult = new CommandResult(); 
        currentWorkingDirectory = new File(System.getProperty("user.dir"));
    }

    public CommandResult getCommandResult() {
        return commandResult;
    }

    public void executeCommand(Command cmd) {
        StringBuilder stdout = new StringBuilder();
        StringBuilder stderr = new StringBuilder();
        int exitCode = 0;

        if (cmd == null || cmd.getCommand().trim().isEmpty()) {
            stdout.append(" ");
            stderr.append("La commande est nulle ou vide.");
            System.err.println(stderr.toString());
            return;
        }

        if (cmd.getCommand().trim().startsWith("cd ")) {
            String path = cmd.getCommand().trim().substring(3).trim();
            File newDirectory = new File(currentWorkingDirectory, path);

            if (newDirectory.exists() && newDirectory.isDirectory()) {
                currentWorkingDirectory = newDirectory.getAbsoluteFile();
                stdout.append(currentWorkingDirectory.getAbsolutePath()).append("\n");
            } else {
                stderr.append("Impossible de trouver le répertoire: ").append(path).append("\n");
                System.err.println(stderr.toString());
            }
            return; 
        }

        
        List<String> commandParts = new ArrayList<>();
        if (System.getProperty("os.name").toLowerCase().contains("win")) { //windows
            commandParts.add("cmd.exe");
            commandParts.add("/c");
            commandParts.add(cmd.getCommand());
        } else { // other images
            commandParts.add("/bin/sh");
            commandParts.add("-c");
            commandParts.add(cmd.getCommand());
        }


        try {
            ProcessBuilder processBuilder = new ProcessBuilder(commandParts);
            processBuilder.directory(currentWorkingDirectory); 

            Process process = processBuilder.start();

            // Lecture de la sortie standard
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                stdout.append(line).append("\n");
            }

            if (!processBuilder.redirectErrorStream()) {
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while ((line = errorReader.readLine()) != null) {
                    stderr.append(line).append("\n");
                }
            }

            exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.print(stdout.toString());
            } else {
                System.err.println("Erreur d'exécution :" + exitCode);
            
                if (stderr.length() > 0) {
                    System.err.println("Sortie d'erreur:\n" + stderr.toString());
                } else if (stdout.length() > 0 && processBuilder.redirectErrorStream()) {
                    System.err.println("Sortie (incluant les erreurs):\n" + stdout.toString());
                }
            }

        } catch (IOException e) {
            stderr.append("Erreur d'E/S lors de l'exécution de la commande: ").append(e.getMessage()).append("\n");
            System.err.println(stderr.toString());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            stderr.append("Le processus a été interrompu: ").append(e.getMessage()).append("\n");
            System.err.println(stderr.toString());
        }
        finally {
            commandResult.setStdout(stdout.toString());
            commandResult.setStderr(stderr.toString());
            commandResult.setExitCode(exitCode);
        }
    }

    public static CommandResult executeCommand(String command, File currentWorkingDirectory) {
        StringBuilder stdout = new StringBuilder();
        StringBuilder stderr = new StringBuilder();
        int exitCode = -1;

        ProcessBuilder processBuilder;
        String[] commandParts;

        // Handle common commands that might require special shell invocation
        if (System.getProperty("os.name").toLowerCase().contains("win")) {
            // For Windows, use cmd.exe /c
            commandParts = new String[]{"cmd.exe", "/c", command};
        } else {
            // For Unix-like systems, use bash -c
            commandParts = new String[]{"/bin/bash", "-c", command};
        }

        processBuilder = new ProcessBuilder(commandParts);
        processBuilder.directory(currentWorkingDirectory); // Set the working directory

        try {
            Process process = processBuilder.start();


            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stdout.append(line).append("\n");
                }
            }


            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    stderr.append(line).append("\n");
                }
            }

            exitCode = process.waitFor();


            if (command.trim().startsWith("cd ")) {
                String path = command.trim().substring(3).trim();
                File newDirectory = new File(currentWorkingDirectory, path);

                if (newDirectory.exists() && newDirectory.isDirectory()) {
                    currentWorkingDirectory = newDirectory.getAbsoluteFile();
                    stdout.append(currentWorkingDirectory.getAbsolutePath()).append("\n");
                } else {
                    stderr.append("Impossible ,Dir not found: ").append(path).append("\n");
                    System.err.println(stderr.toString());
                }
                return new CommandResult(new Command(command), stdout.toString(), stderr.toString(), exitCode);
            }

        } catch (IOException | InterruptedException e) {
            stderr.append("Erreur d'exécution de la commande: ").append(e.getMessage()).append("\n");
            e.printStackTrace();
            exitCode = 1; // Indicate failure
        }

        return new CommandResult(new Command(command),stdout.toString(), stderr.toString(), exitCode);
    }

    /*public static void main(String[] args) {
        CommandExecutorImproved instance = new CommandExecutorImproved();
        Scanner sc = new Scanner(System.in);
        String cmd;

        while (true) {
            System.out.print("@host:~$ ");
            cmd = sc.nextLine();

            if (cmd.equalsIgnoreCase("exit")) { 
                System.out.println("Stop terminal");
                break;
            }
            
            instance.executeCommand(new Command(cmd));
        }
        sc.close();
    }*/

}
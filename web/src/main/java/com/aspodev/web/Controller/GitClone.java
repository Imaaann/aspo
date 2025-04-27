package com.aspodev.web.Controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class GitClone {

    public static void cloneRepository(String repoUrl, String destination) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("git", "clone", repoUrl, destination);
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("Repository cloned successfully to: " + destination);
            } else {
                System.err.println("Failed to clone repository. Exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    

//    public static void main(String[] args) {
//        String repoUrl = "https://github.com/Imaaann/aspo";
//        String destination = "C:\\Users\\Imad\\Desktop\\OO Metric\\New folder";
//
//        if (repoUrl == null || destination == null) {
//            System.out.println("Usage: java GitClone <repository_url> [destination_directory]");
//            return;
//        }
//        cloneRepository(repoUrl, destination);
//    }
}
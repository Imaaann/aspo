package com.aspodev.cli;

import picocli.CommandLine;

public class Main {
    public static void main(String[] args) {
        int exitCode = new CommandLine(new AspoCommand()).execute(args);
        System.exit(exitCode);
    }
}

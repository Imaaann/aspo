package com.aspodev.cli;
import java.lang.Runnable;
import picocli.CommandLine.Option;

public class AspoCommand implements Runnable {

    @Option(names = { "-v", "--verbose" }, description = "Enable Verbose mode.") 
    private boolean verbose;

    @Option(names =  {"-n", "--none"}, description = "Removes the file ouputs")
    private boolean noFileOutputs;

    @Override
    public void run() {
        if (verbose) {
            System.out.println("Verbose mode activated");
        }

        if (noFileOutputs) {
            System.out.println("no file outputs will be shown");
        }
    }
}

package com.aspodev.cli;
import java.lang.Runnable;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command (
    name = "Aspo",
    mixinStandardHelpOptions = true,
    version = "Aspo@0.0.1",
    description = "A static code analyzer for java projects"
)
public class AspoCommand implements Runnable {

    @Option (names = {"-nf", "--no-files"}, description = "Makes the program not output any files")
    private boolean noFileOutputs;

    @Option (names = {"-m", "--more"}, description = "Adds more statistics to the analysis")
    private boolean moreMetrics;

    @Parameters (index = "0", description= "The PATH/URL of the repository to scan")
    private String targetPath;

    @Override
    public void run() {
        System.out.println("Starting analysis of: " + targetPath);
        System.out.println("More flag enabled? " + moreMetrics);
        System.out.println("Hidden output files? " + noFileOutputs);
    }
}

package com.aspodev.cli;
import java.io.IOException;
import java.lang.Runnable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import com.aspodev.cleaner.Cleaner;
import com.aspodev.resolver.PathResolver;

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
        // Temporary Timing for checking the execute time
        long start = System.currentTimeMillis();


        PathResolver pathResolver = new PathResolver(targetPath);

        List<Path> javaFilePaths = pathResolver.getAllJavaPaths();

        for (Path javaPath : javaFilePaths) {
            
            StringBuilder contents;

            try {
                contents = new StringBuilder(Files.readString(javaPath));
                Cleaner.cleanFile(contents);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        // Temporary Timing for checking the execute time
        long end  = System.currentTimeMillis();
        System.out.println("[DEBUG] == EXECUTION TIME: " + (end - start));
    }
}

package com.aspodev.cli;

import java.lang.Runnable;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import javax.print.attribute.standard.MediaSize.Other;

import com.aspodev.Calculator.MetricCalculator;
import com.aspodev.Calculator.Metrics;
import com.aspodev.Calculator.SystemCalculator;
import com.aspodev.SCAR.Model;
import com.aspodev.TypeParser.TypeParser;
import com.aspodev.parser.Parser;
import com.aspodev.resolver.PathResolver;
import com.aspodev.utils.OtherTools;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "Aspo", mixinStandardHelpOptions = true, version = "Aspo@0.0.1", description = "A static code analyzer for java projects")
public class AspoCommand implements Runnable {

    @Option(names = { "-nf", "--no-files" }, description = "Makes the program not output any files")
    private boolean noFileOutputs;

    @Option(names = { "-m", "--more" }, description = "Adds more statistics to the analysis")
    private boolean moreMetrics;

    @Option(names = { "--dev", "-d" }, arity = "1", description = "Enables parsing of a certain file via index")
    private Integer devMode;

    @Parameters(index = "0", description = "The PATH/URL of the repository to scan")
    private String targetPath;

    @Override
    public void run() {
        // Temporary Timing for checking the execute time
        long start = System.currentTimeMillis();

        PathResolver pathResolver = new PathResolver(targetPath);
        List<Path> javaFilePaths = pathResolver.getAllJavaPaths();

        TypeParser typeParser = new TypeParser(javaFilePaths);
        Model SCARModel = new Model();

        if (devMode != null) {
            System.out.println("[DEBUG] == File Parsing (" + javaFilePaths.get(devMode).getFileName() + ")");
            Parser parser = new Parser(javaFilePaths.get(devMode), typeParser, SCARModel);
            parser.parse();
        } else {
            for (Path p : javaFilePaths) {
                System.out.println("[DEBUG] == File Parsing (" + p.getFileName() + ")");
                Parser parser = new Parser(p, typeParser, SCARModel);
                parser.parse();
            }
        }

        SCARModel.createInheritanceGraph();

        System.out.println("[DEBUG] == Output model: " + SCARModel);

        System.out.println("[DEBUG] Model cohesion: " + SCARModel.getCohesionBreaker());

        // Next step is to execute the metric calculation and get our results map
        int threads = Runtime.getRuntime().availableProcessors();
        SystemCalculator calculator = new SystemCalculator(threads);
        Map<String, Metrics> results = calculator.calculateMetrics(SCARModel);
        System.out.println("[DEBUG] Results map: ");
        System.out.println(OtherTools.resultMapToString(results));
        // Temporary Timing for checking the execute time
        long end = System.currentTimeMillis();
        System.out.println("[DEBUG] == EXECUTION TIME: " + (end - start));
    }
}

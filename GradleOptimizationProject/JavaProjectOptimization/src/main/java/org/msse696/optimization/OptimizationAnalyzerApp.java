package org.msse696.optimization;

import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import org.msse696.automation.InefficiencyAnalyzerRunner;
import org.msse696.optimization.helper.debug.Debug;
import org.msse696.optimization.helper.report.CombinedAnalysisReportGenerator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Command-line application to analyze Java code for optimization inefficiencies.
 */
public class OptimizationAnalyzerApp {

    /**
     * Main entry point of the application.
     *
     * @param args Command-line arguments: [directory_path] [createReport(true|false)] [reportFileName].
     */
    public static void main(String[] args) {
        boolean createReport = true;
        String directoryPath = "";
        String outputReportPath = "target/results/reports/directory_analysis_report.html";

        preSetup();
        if (args.length > 3) {
            Debug.error("Usage: java DirectoryAnalyzer <directory_path> [true|false] <fileName>");
            return;
        }
        if (args.length > 0) {
            directoryPath = args[0];
        } else {
            directoryPath = "C:/Users/Rajesh/Documents/GitHub/MSSE696SftwrEngineeringPracticum/GradleOptimizationProject"; // Default path
        }
        if (args.length == 2) {
            createReport = Boolean.parseBoolean(args[1]);
        }
        if (args.length == 3) {
            outputReportPath = Paths.get(directoryPath, args[2]).toString();
        }

        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            Debug.error("Invalid directory path: " + directoryPath);
            return;
        }

        List<File> javaFiles = findJavaFiles(directoryPath);
        System.out.println("Total Java Files being Analyzed: " + javaFiles.size());
        List<InefficiencyAnalyzerRunner> runners = new ArrayList<>();
        int grandTotalInefficiencies = 0;

        if (javaFiles.isEmpty()) {
            Debug.info("No Java files found.");
            CombinedAnalysisReportGenerator.generateReport(runners, outputReportPath, createReport);
            return;
        }

        Debug.info("Found the following Java files:");
        for (File javaFile : javaFiles) {
            Debug.info("- " + javaFile.getAbsolutePath());
            InefficiencyAnalyzerRunner runner = new InefficiencyAnalyzerRunner();
            runner.analyze(javaFile, false);
            runners.add(runner);
            String[][] recommendations = runner.getRecommendedData();
            if (recommendations != null && recommendations.length > 0) {
                grandTotalInefficiencies += runner.getTotalInefficiencyCount();
            }
        }
        System.out.println("Total # of inefficiencies: " + grandTotalInefficiencies);
        CombinedAnalysisReportGenerator.generateReport(runners, outputReportPath, createReport);
        Debug.info("Analysis complete. Report generated at: " + outputReportPath);
    }

    /**
     * Finds all Java files in the given directory and its subdirectories.
     *
     * @param directoryPath The path to the directory.
     * @return A list of Java files.
     */
    private static List<File> findJavaFiles(String directoryPath) {
        List<File> javaFiles = new ArrayList<>();
        try (Stream<Path> pathStream = Files.walk(Paths.get(directoryPath))) {
            javaFiles = pathStream
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .filter(file -> file.getName().endsWith(".java"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            Debug.error("Error traversing directory: " + e.getMessage());
        }
        return javaFiles;
    }

    /**
     * Configures the JavaParser.
     */
    private static void preSetup() {
        ParserConfiguration config = new ParserConfiguration();
        config.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_17);
        StaticJavaParser.setConfiguration(config);
    }
}
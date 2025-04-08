package org.msse696.automation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class StringConcatenationAnalyzerTest {
    File efficientFile = new File("src/main/java/org/msse696/optimization/efficient/StringConcatenationEfficient.java");
    File inefficientFile = new File("src/main/java/org/msse696/optimization/inefficient/StringConcatenationInefficient.java");

    Analyzer analyzer;

    @Test
    void testAnalyze_WithInefficientCode() {
        analyzer = new StringConcatenationAnalyzer();

        boolean inefficienciesDetected = analyzer.analyze(inefficientFile, true);
        System.out.println("Inefficiencies detected: " + inefficienciesDetected);

        // Check that inefficiencies are detected
        Assertions.assertTrue(inefficienciesDetected, "Inefficiencies should be detected in the code.");

        // Check that the HTML report is created
        File reportFile = new File(analyzer.getReport());
        Assertions.assertTrue(reportFile.exists(), "HTML report should be created for inefficient string concatenation.");
    }

    @Test
    void testAnalyze_WithEfficientCode() {
        analyzer = new StringConcatenationAnalyzer();
        boolean inefficienciesDetected = analyzer.analyze(efficientFile, true);

        System.out.println("Inefficiencies detected: " + inefficienciesDetected);
        Assertions.assertFalse(inefficienciesDetected, "No inefficiencies should be detected in the code.");
    }

    @Test
    void testAnalyze_WithEfficientCode1() {
        analyzer = new StringConcatenationAnalyzer();
        boolean inefficienciesDetected = analyzer.analyze(efficientFile, true);

        System.out.println("Inefficiencies detected: " + inefficienciesDetected);

        // Check that no inefficiencies are detected
        Assertions.assertFalse(inefficienciesDetected, "No inefficiencies should be detected in the code.");
    }

    @Test
    void testAnalyze_ReportFileGenerated() {

        analyzer = new StringConcatenationAnalyzer();
        boolean inefficienciesDetected = analyzer.analyze(inefficientFile, true);

        System.out.println("Inefficiencies detected: " + inefficienciesDetected);

        // Verify that the report file is created
        File reportFile = new File(analyzer.getReport());
        Assertions.assertTrue(reportFile.exists(), "HTML report should be created when inefficiencies are detected.");
    }
}

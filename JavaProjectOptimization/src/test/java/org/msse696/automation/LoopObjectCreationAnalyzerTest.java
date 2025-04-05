package org.msse696.automation;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

public class LoopObjectCreationAnalyzerTest {

    @Test
    void testAnalyze_WithCustomClass() {
        class Custom {
            public void testMethod() {
                for (int i = 0; i < 10; i++) {
                    String obj = new String("Test");
                }
            }
        }

        LoopObjectCreationAnalyzer analyzer = new LoopObjectCreationAnalyzer();
        boolean optimizationNeeded = analyzer.analyze(Custom.class);

        System.out.println("Optimization needed: " + optimizationNeeded);

        File reportFile = new File(analyzer.getReportName());
        Assertions.assertTrue(optimizationNeeded, "Optimization should be required for inefficient loop creation.");
        Assertions.assertTrue(reportFile.exists(), "HTML report should be created for inefficient loop creation.");
    }

    //@Test
    void testAnalyze_WithEfficientCode() {
        class Efficient {
            public void testMethod() {
                String obj = new String("Test");
                for (int i = 0; i < 10; i++) {
                    // Reuse object
                }
            }
        }

        LoopObjectCreationAnalyzer analyzer = new LoopObjectCreationAnalyzer();
        boolean optimizationNeeded = analyzer.analyze(Efficient.class);

        System.out.println("Optimization needed: " + optimizationNeeded);

        File reportFile = new File(analyzer.getReportName());
        Assertions.assertFalse(optimizationNeeded, "Optimization should NOT be required for efficient loop creation.");
        Assertions.assertFalse(reportFile.exists(), "HTML report should NOT be created for efficient loop creation.");
    }
}

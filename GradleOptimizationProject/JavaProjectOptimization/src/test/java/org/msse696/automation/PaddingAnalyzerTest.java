package org.msse696.automation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PaddingAnalyzerTest {

    private static final String INEFFICIENT_FILE_PATH = "src/test/java/org/msse696/automation/testfiles/PaddingTestClassInefficient.java";
    private static final String EFFICIENT_FILE_PATH = "src/test/java/org/msse696/automation/testfiles/PaddingTestClassEfficient.java";
    private PaddingAnalyzer paddingAnalyzer;

    @Test
    void testGetFieldSize_Primitives() {
        paddingAnalyzer = new PaddingAnalyzer();
        assertEquals(1, paddingAnalyzer.getFieldSize("boolean"));
        assertEquals(1, paddingAnalyzer.getFieldSize("byte"));
        assertEquals(2, paddingAnalyzer.getFieldSize("char"));
        assertEquals(4, paddingAnalyzer.getFieldSize("int"));
        assertEquals(8, paddingAnalyzer.getFieldSize("long"));
    }

    @Test
    void testGetFieldSize_UnknownType() {
        paddingAnalyzer = new PaddingAnalyzer();
        assertEquals(-1, paddingAnalyzer.getFieldSize("UnknownType"));
    }

    @Test
    void testAnalyzeOptimizationNeeded() {
        paddingAnalyzer = new PaddingAnalyzer();

        // Provide the file with inefficient field arrangement
        File inefficientFile = new File(INEFFICIENT_FILE_PATH);
        boolean optimizationNeeded = paddingAnalyzer.analyze(inefficientFile, true);

        assertTrue(optimizationNeeded, "Optimization should be needed for an inefficient file.");
    }

    @Test
    void testAnalyzeNoOptimizationNeeded() {
        paddingAnalyzer = new PaddingAnalyzer();

        // Provide the file with efficient field arrangement
        File efficientFile = new File(EFFICIENT_FILE_PATH);
        boolean optimizationNeeded = paddingAnalyzer.analyze(efficientFile, true);

        assertFalse(optimizationNeeded, "Optimization should not be needed for an efficient file.");
    }

    @Test
    void testIsSameOrder() {
        paddingAnalyzer = new PaddingAnalyzer();

        List<PaddingAnalyzer.FieldAnalysis> actualOrder = new ArrayList<>();
        actualOrder.add(new PaddingAnalyzer.FieldAnalysis("field1", "int", 4));
        actualOrder.add(new PaddingAnalyzer.FieldAnalysis("field2", "long", 8));

        List<PaddingAnalyzer.FieldAnalysis> recommendedOrder = new ArrayList<>();
        recommendedOrder.add(new PaddingAnalyzer.FieldAnalysis("field2", "long", 8));
        recommendedOrder.add(new PaddingAnalyzer.FieldAnalysis("field1", "int", 4));

        assertFalse(PaddingAnalyzer.isSameOrder(actualOrder, recommendedOrder), "Orders should not match for inefficient arrangement.");

        // Test with matching orders
        recommendedOrder.clear();
        recommendedOrder.add(new PaddingAnalyzer.FieldAnalysis("field1", "int", 4));
        recommendedOrder.add(new PaddingAnalyzer.FieldAnalysis("field2", "long", 8));

        assertTrue(PaddingAnalyzer.isSameOrder(actualOrder, recommendedOrder), "Orders should match for efficient arrangement.");
    }

    @Test
    void testAnalyzeAndNoReport_EfficientFile() {
        paddingAnalyzer = new PaddingAnalyzer();

        // Provide the efficient file for analysis
        File efficientFile = new File(EFFICIENT_FILE_PATH);
        boolean optimizationNeeded = paddingAnalyzer.analyze(efficientFile, true);

        assertFalse(optimizationNeeded, "Optimization should NOT be required for the efficient file.");
      }

    @Test
    void testAnalyzeAndCreateReport_InefficientFile() {
        String outputFilePath = "target/results/reports/padding_report.html";

        File reportFile = new File(outputFilePath);
        if (reportFile.exists()) {
            reportFile.delete();
        }

        paddingAnalyzer = new PaddingAnalyzer();

        // Provide the inefficient file for analysis
        File inefficientFile = new File(INEFFICIENT_FILE_PATH);
        boolean optimizationNeeded = paddingAnalyzer.analyze(inefficientFile, true);

        assertTrue(optimizationNeeded, "Optimization should be required for the inefficient file.");
        assertTrue(reportFile.exists(), "HTML report should be created for the inefficient file.");
    }
}

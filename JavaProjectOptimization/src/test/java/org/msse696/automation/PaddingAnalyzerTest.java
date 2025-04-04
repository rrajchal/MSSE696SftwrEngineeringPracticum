package org.msse696.automation;

import org.junit.jupiter.api.Test;

import static java.nio.file.Paths.get;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class PaddingAnalyzerTest {

    // Helper class for testing
    static class TestClassInefficient {
        boolean flag;
        long id;
        char grade;
        int value;
    }

    static class TestClassEfficient {
        long id;
        int value;
        char grade;
        boolean flag;
    }

    @Test
    void testGetFieldSize_Primitives() {
        assertEquals(1, PaddingAnalyzer.getFieldSize("boolean"));
        assertEquals(1, PaddingAnalyzer.getFieldSize("byte"));
        assertEquals(2, PaddingAnalyzer.getFieldSize("char"));
        assertEquals(4, PaddingAnalyzer.getFieldSize("int"));
        assertEquals(8, PaddingAnalyzer.getFieldSize("long"));
    }

    @Test
    void testGetFieldSize_UnknownType() {
        assertEquals(-1, PaddingAnalyzer.getFieldSize("UnknownType"));
    }

    @Test
    void testAnalyzeOptimizationNeeded() {
        // Testing with an inefficient class
        boolean optimizationNeeded = PaddingAnalyzer.analyze(TestClassInefficient.class);

        assertTrue(optimizationNeeded, "Optimization should be needed for an inefficient class.");
    }

    @Test
    void testAnalyzeNoOptimizationNeeded() {
        // Testing with an already optimized class
        boolean optimizationNeeded = PaddingAnalyzer.analyze(TestClassEfficient.class);

        assertFalse(optimizationNeeded, "Optimization should not be needed for an efficient class.");
    }

    @Test
    void testIsSameOrder() {
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
    void testAnalyzeAndNoReport_EfficientClass() {
        // Output file path for the report
        String outputFilePath = "src/results/reports/padding_report.html";

        // Ensure the report does not exist before analysis
        File reportFile = new File(outputFilePath);
        if (reportFile.exists()) {
            reportFile.delete();
        }

        // Analyze the efficient class
        boolean optimizationNeeded = PaddingAnalyzer.analyze(TestClassEfficient.class);

        // Assert that no report is generated and no optimization is needed
        assertFalse(optimizationNeeded, "Optimization should NOT be required for the efficient class.");
        assertFalse(reportFile.exists(), "HTML report should NOT be created for the efficient class.");
    }

    @Test
    void testAnalyzeAndCreateReport_InefficientClass() {
        // Path to report
        String outputFilePath = PaddingAnalyzer.getReportName();

        // Ensure the report does not exist before test
        File reportFile = new File(outputFilePath);
        if (reportFile.exists()) {
            assertTrue(reportFile.delete());
        }

        // Analyze inefficient class and create report
        boolean optimizationNeeded = PaddingAnalyzer.analyze(TestClassInefficient.class);

        // Assertions
        assertTrue(optimizationNeeded, "Optimization should be required for the inefficient class.");
        assertTrue(reportFile.exists(), "HTML report should be created for the inefficient class.");
    }
}

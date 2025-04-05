package org.msse696.automation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

class PaddingAnalyzerTest {

    private PaddingAnalyzer paddingAnalyzer;

    // Helper classes for testing
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

    static class TestClassInefficientWithMethods {
        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public char getGrade() {
            return grade;
        }

        public void setGrade(char grade) {
            this.grade = grade;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        boolean flag;
        long id;
        char grade;
        int value;
    }

    static class TestClassEfficientWithMethods {
        long id;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public char getGrade() {
            return grade;
        }

        public void setGrade(char grade) {
            this.grade = grade;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }

        int value;
        char grade;
        boolean flag;
    }

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
        boolean optimizationNeeded = paddingAnalyzer.analyze(TestClassInefficient.class);
        assertTrue(optimizationNeeded, "Optimization should be needed for an inefficient class.");
    }

    @Test
    void testAnalyzeNoOptimizationNeeded() {
        paddingAnalyzer = new PaddingAnalyzer();
        boolean optimizationNeeded = paddingAnalyzer.analyze(TestClassEfficient.class);
        assertFalse(optimizationNeeded, "Optimization should not be needed for an efficient class.");
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
    void testAnalyzeAndNoReport_EfficientClass() {
        String outputFilePath = "target/results/reports/padding_report.html";

        File reportFile = new File(outputFilePath);
        if (reportFile.exists()) {
            reportFile.delete();
        }

        paddingAnalyzer = new PaddingAnalyzer();
        boolean optimizationNeeded = paddingAnalyzer.analyze(TestClassEfficient.class);

        assertFalse(optimizationNeeded, "Optimization should NOT be required for the efficient class.");
        assertFalse(reportFile.exists(), "HTML report should NOT be created for the efficient class.");
    }

    @Test
    void testAnalyzeAndCreateReport_InefficientClass() {
        String outputFilePath = paddingAnalyzer == null ? new PaddingAnalyzer().getReportName() : paddingAnalyzer.getReportName();

        File reportFile = new File(outputFilePath);
        if (reportFile.exists()) {
            assertTrue(reportFile.delete());
        }

        paddingAnalyzer = new PaddingAnalyzer();
        boolean optimizationNeeded = paddingAnalyzer.analyze(TestClassInefficient.class);

        assertTrue(optimizationNeeded, "Optimization should be required for the inefficient class.");
        assertTrue(reportFile.exists(), "HTML report should be created for the inefficient class.");
    }

    @Test
    void testAnalyzeAndCreateReport_InefficientClassWithMethods() {
        String outputFilePath = paddingAnalyzer == null ? new PaddingAnalyzer().getReportName() : paddingAnalyzer.getReportName();

        File reportFile = new File(outputFilePath);
        if (reportFile.exists()) {
            assertTrue(reportFile.delete());
        }

        paddingAnalyzer = new PaddingAnalyzer();
        boolean optimizationNeeded = paddingAnalyzer.analyze(TestClassInefficientWithMethods.class);

        assertTrue(optimizationNeeded, "Optimization should be required for the inefficient class.");
        assertTrue(reportFile.exists(), "HTML report should be created for the inefficient class.");
    }
}

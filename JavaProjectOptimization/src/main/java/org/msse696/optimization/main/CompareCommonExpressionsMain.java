package org.msse696.optimization.main;

import org.msse696.optimization.compare.CompareCommonExpressions;
import org.msse696.optimization.helper.DataAnalyzer;
import org.msse696.optimization.helper.HeapMonitor;

import java.util.List;

public class CompareCommonExpressionsMain {
    private static final int NUMBER_OF_ITERATIONS = 10_000_000;

    public static void main(String[] args) {
        System.out.println("Analyzing common expression elimination performance...");

        //System.out.println("\nRunning efficient first, then inefficient...");
        //runEfficientFirstThenInefficient();

        System.out.println("Running inefficient first, then efficient...");
        runInefficientFirstThenEfficient();
    }

    private static void runEfficientFirstThenInefficient() {

        createData(true, NUMBER_OF_ITERATIONS);
        // Efficient calculation
        HeapMonitor.startMonitoring();
        List<Double> efficientData = createData(true, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double efficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        HeapMonitor.resetPeakHeapUsage();

        createData(false, NUMBER_OF_ITERATIONS);
        // Inefficient calculation
        HeapMonitor.startMonitoring();
        List<Double> inefficientData = createData(false, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double inefficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        analyzeData(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage);
    }

    private static void runInefficientFirstThenEfficient() {
        createData(false, NUMBER_OF_ITERATIONS);
        // Inefficient calculation
        HeapMonitor.startMonitoring();
        List<Double> inefficientData = createData(false, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double inefficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        HeapMonitor.resetPeakHeapUsage();

        createData(true, NUMBER_OF_ITERATIONS);
        // Efficient calculation
        HeapMonitor.startMonitoring();
        List<Double> efficientData = createData(true, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double efficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        analyzeData(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage);
    }

    private static List<Double> createData(boolean isEfficient, int iterations) {
        CompareCommonExpressions comparator = new CompareCommonExpressions(isEfficient, iterations);
        return comparator.getExecutionTimes();
    }

    private static void analyzeData(List<Double> efficientData, List<Double> inefficientData,
                                    double efficientPeakHeapUsage, double inefficientPeakHeapUsage) {
        DataAnalyzer dataAnalyzer = new DataAnalyzer();
        dataAnalyzer.displayStatisticalAnalysis(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage);
    }
}

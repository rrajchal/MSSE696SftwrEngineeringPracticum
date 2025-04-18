package org.msse696.optimization.main;

import org.msse696.optimization.compare.Hypo7CompareTypeCheckingPerformance;
import org.msse696.optimization.helper.DataAnalyzer;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.helper.HeapMonitor;
import org.msse696.optimization.helper.debug.Debug;

import java.util.List;

public class Hypo7CompareTypeCheckingPerformanceMain {
    private static final int NUMBER_OF_ITERATIONS = 1000;
    private static final String RESULTS_DIRECTORY = "src/results/";
    private static final String RESULTS_FILENAME = RESULTS_DIRECTORY + "hypothesis7_type_checking_efficiency.txt";

    public static void main(String[] args) {
        Debug.info("Analyzing type checking performance...");

        Debug.info("Running efficient first, then inefficient...");
        for (int i = 0; i < 10; i++)
            runEfficientFirstThenInefficient();

        Debug.info("\nRunning inefficient first, then efficient...");
        for (int i = 0; i < 10; i++)
            runInefficientFirstThenEfficient();
    }

    private static void runEfficientFirstThenInefficient() {
        // JVM warm-up with efficient data generation
        createData(true, NUMBER_OF_ITERATIONS);

        // Efficient type checking
        HeapMonitor.startMonitoring();
        List<Double> efficientData = createData(true, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double efficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        HeapMonitor.resetPeakHeapUsage();

        // JVM warm-up with inefficient data generation
        createData(false, NUMBER_OF_ITERATIONS);

        // Inefficient type checking
        HeapMonitor.startMonitoring();
        List<Double> inefficientData = createData(false, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double inefficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        analyzeData(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage);
    }

    private static void runInefficientFirstThenEfficient() {
        // JVM warm-up with inefficient data generation
        createData(false, NUMBER_OF_ITERATIONS);

        // Inefficient type checking
        HeapMonitor.startMonitoring();
        List<Double> inefficientData = createData(false, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double inefficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        HeapMonitor.resetPeakHeapUsage();

        // JVM warm-up with efficient data generation
        createData(true, NUMBER_OF_ITERATIONS);

        // Efficient type checking
        HeapMonitor.startMonitoring();
        List<Double> efficientData = createData(true, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double efficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        analyzeData(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage);
    }

    private static List<Double> createData(boolean isEfficient, int iterations) {
        Hypo7CompareTypeCheckingPerformance comparator = new Hypo7CompareTypeCheckingPerformance(isEfficient, iterations);
        return comparator.getExecutionTimes();
    }

    private static void analyzeData(List<Double> efficientData, List<Double> inefficientData, 
                                     double efficientPeakHeapUsage, double inefficientPeakHeapUsage) {
        FileDataManager fileManager = new FileDataManager(RESULTS_FILENAME);
        DataAnalyzer dataAnalyzer = new DataAnalyzer();

        dataAnalyzer.displayStatisticalAnalysis(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage, fileManager);
    }
}

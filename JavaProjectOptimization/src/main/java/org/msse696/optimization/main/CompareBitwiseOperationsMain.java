package org.msse696.optimization.main;

import org.msse696.optimization.compare.CompareBitwiseOperations;
import org.msse696.optimization.helper.DataAnalyzer;
import org.msse696.optimization.helper.HeapMonitor;
import org.msse696.optimization.helper.FileDataManager;

import java.util.List;

public class CompareBitwiseOperationsMain {
    private static final int NUMBER_OF_ITERATIONS = 10_000_000;
    private static final String RESULTS_DIRECTORY = "src/results/";
    private static final String RESULTS_FILENAME = RESULTS_DIRECTORY + "hypothesis4_bitwise_arithmetic.txt";

    public static void main(String[] args) {
        System.out.println("Analyzing bitwise vs arithmetic performance...");

        //System.out.println("Running efficient first, then inefficient...");
        //for (int i = 0; i < 5; i++)
        //    runEfficientFirstThenInefficient();

        //System.out.println("Running inefficient first, then efficient...");
        for (int i = 0; i < 5; i++)
            runInefficientFirstThenEfficient();
    }

    private static void runInefficientFirstThenEfficient() {

        // JVM warm-up with efficient data generation
        createData(false, NUMBER_OF_ITERATIONS);

        // Inefficient calculation
        HeapMonitor.startMonitoring();
        List<Double> inefficientData = createData(false, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double inefficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        HeapMonitor.resetPeakHeapUsage();

        // JVM warm-up with inefficient data generation
        createData(true, NUMBER_OF_ITERATIONS);

        // Efficient calculation
        HeapMonitor.startMonitoring();
        List<Double> efficientData = createData(true, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double efficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        analyzeData(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage);
    }

    private static void runEfficientFirstThenInefficient() {
        // Efficient calculation
        HeapMonitor.startMonitoring();
        List<Double> efficientData = createData(true, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double efficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        HeapMonitor.resetPeakHeapUsage();

        // Inefficient calculation
        HeapMonitor.startMonitoring();
        List<Double> inefficientData = createData(false, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double inefficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        analyzeData(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage);
    }

    private static List<Double> createData(boolean isEfficient, int iterations) {
        CompareBitwiseOperations comparator = new CompareBitwiseOperations(isEfficient, iterations);
        return comparator.getExecutionTimes();
    }

    private static void analyzeData(List<Double> efficientData, List<Double> inefficientData,
                                    double efficientPeakHeapUsage, double inefficientPeakHeapUsage) {
        FileDataManager fileManager = new FileDataManager(RESULTS_FILENAME);
        DataAnalyzer dataAnalyzer = new DataAnalyzer();

        dataAnalyzer.displayStatisticalAnalysis(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage, fileManager);
    }
}

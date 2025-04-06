package org.msse696.optimization.main;

import org.msse696.optimization.compare.Hypo6CompareTryCatchPerformance;
import org.msse696.optimization.helper.DataAnalyzer;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.helper.HeapMonitor;

import java.util.List;

public class Hypo6CompareTryCatchPerformanceMain {
    private static final int NUMBER_OF_ITERATIONS = 10000;
    private static final String RESULTS_DIRECTORY = "src/results/";
    private static final String RESULTS_FILENAME = RESULTS_DIRECTORY + "hypothesis6_try_catch_efficiency.txt";

    public static void main(String[] args) {
        System.out.println("Analyzing try-catch placement performance...");

        System.out.println("Running efficient first, then inefficient...");
        //for (int i = 0; i < 3; i++)
        runEfficientFirstThenInefficient();

        System.out.println("\nRunning inefficient first, then efficient...");
        //for (int i = 0; i < 5; i++)
       // runInefficientFirstThenEfficient();
    }

    private static void runEfficientFirstThenInefficient() {
        // JVM warm-up with efficient data generation
        createData(true, NUMBER_OF_ITERATIONS);

        // Efficient calculation
        HeapMonitor.startMonitoring();
        List<Double> efficientData = createData(true, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double efficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        HeapMonitor.resetPeakHeapUsage();

        // JVM warm-up with inefficient data generation
        createData(false, NUMBER_OF_ITERATIONS);

        // Inefficient calculation
        HeapMonitor.startMonitoring();
        List<Double> inefficientData = createData(false, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double inefficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        analyzeData(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage);
    }

    private static void runInefficientFirstThenEfficient() {
        // JVM warm-up with inefficient data generation
        createData(false, NUMBER_OF_ITERATIONS);

        // Inefficient calculation
        HeapMonitor.startMonitoring();
        List<Double> inefficientData = createData(false, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double inefficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        HeapMonitor.resetPeakHeapUsage();

        // JVM warm-up with efficient data generation
        createData(true, NUMBER_OF_ITERATIONS);

        // Efficient calculation
        HeapMonitor.startMonitoring();
        List<Double> efficientData = createData(true, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double efficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        analyzeData(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage);
    }

    private static List<Double> createData(boolean isEfficient, int iterations) {
        Hypo6CompareTryCatchPerformance comparator = new Hypo6CompareTryCatchPerformance(isEfficient, iterations);
        return comparator.getExecutionTimes();
    }

    private static void analyzeData(List<Double> efficientData, List<Double> inefficientData, 
                                     double efficientPeakHeapUsage, double inefficientPeakHeapUsage) {
        FileDataManager fileManager = new FileDataManager(RESULTS_FILENAME);
        DataAnalyzer dataAnalyzer = new DataAnalyzer();

        dataAnalyzer.displayStatisticalAnalysis(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage, fileManager);
    }
}

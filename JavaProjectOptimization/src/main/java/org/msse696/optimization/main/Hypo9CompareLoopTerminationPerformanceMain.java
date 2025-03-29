package org.msse696.optimization.main;

import org.msse696.optimization.compare.Hypo9CompareLoopTerminationPerformance;
import org.msse696.optimization.helper.DataAnalyzer;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.helper.HeapMonitor;

import java.util.List;

public class Hypo9CompareLoopTerminationPerformanceMain {
    private static final int NUMBER_OF_ITERATIONS = 10_000;
    private static final String RESULTS_DIRECTORY = "src/results/";
    private static final String RESULTS_FILENAME = RESULTS_DIRECTORY + "hypothesis9_loop_termination_efficiency.txt";

    public static void main(String[] args) {
        System.out.println("Analyzing loop termination performance...");

        System.out.println("Running efficient first, then inefficient...");
        //for (int i = 0; i < 5; i++)
        //    runEfficientFirstThenInefficient();

        System.out.println("\nRunning inefficient first, then efficient...");
        //for (int i = 0; i < 5; i++)
            runInefficientFirstThenEfficient();
    }

    private static void runEfficientFirstThenInefficient() {
        // JVM warm-up with efficient loop
        createData(true, NUMBER_OF_ITERATIONS);

        // Efficient loop termination
        HeapMonitor.startMonitoring();
        List<Double> efficientData = createData(true, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double efficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        HeapMonitor.resetPeakHeapUsage();

        // JVM warm-up with inefficient loop
        createData(false, NUMBER_OF_ITERATIONS);

        // Inefficient loop termination
        HeapMonitor.startMonitoring();
        List<Double> inefficientData = createData(false, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double inefficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        analyzeData(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage);
    }

    private static void runInefficientFirstThenEfficient() {
        // JVM warm-up with inefficient loop
        createData(false, NUMBER_OF_ITERATIONS);

        // Inefficient loop termination
        HeapMonitor.startMonitoring();
        List<Double> inefficientData = createData(false, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double inefficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        HeapMonitor.resetPeakHeapUsage();

        // JVM warm-up with efficient loop
        createData(true, NUMBER_OF_ITERATIONS);

        // Efficient loop termination
        HeapMonitor.startMonitoring();
        List<Double> efficientData = createData(true, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double efficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        analyzeData(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage);
    }

    private static List<Double> createData(boolean isEfficient, int iterations) {
        Hypo9CompareLoopTerminationPerformance comparator = new Hypo9CompareLoopTerminationPerformance(isEfficient, iterations);
        return comparator.getExecutionTimes();
    }

    private static void analyzeData(List<Double> efficientData, List<Double> inefficientData, 
                                     double efficientPeakHeapUsage, double inefficientPeakHeapUsage) {
        FileDataManager fileManager = new FileDataManager(RESULTS_FILENAME);
        DataAnalyzer dataAnalyzer = new DataAnalyzer();

        dataAnalyzer.displayStatisticalAnalysis(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage, fileManager);
    }
}

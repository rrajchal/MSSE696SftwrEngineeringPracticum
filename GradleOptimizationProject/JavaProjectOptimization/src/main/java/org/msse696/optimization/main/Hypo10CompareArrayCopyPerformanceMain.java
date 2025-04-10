package org.msse696.optimization.main;

import org.msse696.optimization.compare.Hypo10CompareArrayCopyPerformance;
import org.msse696.optimization.helper.DataAnalyzer;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.helper.HeapMonitor;
import org.msse696.optimization.helper.debug.Debug;

import java.util.List;

public class Hypo10CompareArrayCopyPerformanceMain {
    private static final int NUMBER_OF_ITERATIONS = 100_000;
    private static final int ARRAY_SIZE = 10_000; // Array size for testing
    private static final String RESULTS_DIRECTORY = "src/results/";
    private static final String RESULTS_FILENAME = RESULTS_DIRECTORY + "hypothesis10_array_copy_efficiency.txt";

    public static void main(String[] args) {
        Debug.info("Analyzing array copying performance...");

        Debug.info("Running efficient first, then inefficient...");
        //for (int i = 0; i < 5; i++)
        //    runEfficientFirstThenInefficient();

        Debug.info("\nRunning inefficient first, then efficient...");
        for (int i = 0; i < 5; i++)
           runInefficientFirstThenEfficient();
    }

    private static void runEfficientFirstThenInefficient() {
        // JVM warm-up with efficient copying
        createData(true, NUMBER_OF_ITERATIONS, ARRAY_SIZE);

        // Efficient array copying
        HeapMonitor.startMonitoring();
        List<Double> efficientData = createData(true, NUMBER_OF_ITERATIONS, ARRAY_SIZE);
        HeapMonitor.stopMonitoring();
        double efficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        HeapMonitor.resetPeakHeapUsage();

        // JVM warm-up with inefficient copying
        createData(false, NUMBER_OF_ITERATIONS, ARRAY_SIZE);

        // Inefficient array copying
        HeapMonitor.startMonitoring();
        List<Double> inefficientData = createData(false, NUMBER_OF_ITERATIONS, ARRAY_SIZE);
        HeapMonitor.stopMonitoring();
        double inefficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        analyzeData(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage);
    }

    private static void runInefficientFirstThenEfficient() {
        // JVM warm-up with inefficient copying
        createData(false, NUMBER_OF_ITERATIONS, ARRAY_SIZE);

        // Inefficient array copying
        HeapMonitor.startMonitoring();
        List<Double> inefficientData = createData(false, NUMBER_OF_ITERATIONS, ARRAY_SIZE);
        HeapMonitor.stopMonitoring();
        double inefficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        HeapMonitor.resetPeakHeapUsage();

        // JVM warm-up with efficient copying
        createData(true, NUMBER_OF_ITERATIONS, ARRAY_SIZE);

        // Efficient array copying
        HeapMonitor.startMonitoring();
        List<Double> efficientData = createData(true, NUMBER_OF_ITERATIONS, ARRAY_SIZE);
        HeapMonitor.stopMonitoring();
        double efficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        analyzeData(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage);
    }

    private static List<Double> createData(boolean isEfficient, int iterations, int arraySize) {
        Hypo10CompareArrayCopyPerformance comparator = new Hypo10CompareArrayCopyPerformance(isEfficient, iterations, arraySize);
        return comparator.getExecutionTimes();
    }

    private static void analyzeData(List<Double> efficientData, List<Double> inefficientData, 
                                     double efficientPeakHeapUsage, double inefficientPeakHeapUsage) {
        FileDataManager fileManager = new FileDataManager(RESULTS_FILENAME);
        DataAnalyzer dataAnalyzer = new DataAnalyzer();

        dataAnalyzer.displayStatisticalAnalysis(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage, fileManager);
    }
}

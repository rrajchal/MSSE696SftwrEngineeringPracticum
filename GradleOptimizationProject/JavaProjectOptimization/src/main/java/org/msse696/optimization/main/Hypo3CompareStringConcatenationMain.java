package org.msse696.optimization.main;

import org.msse696.optimization.compare.Hypo3CompareStringConcatenation;
import org.msse696.optimization.helper.DataAnalyzer;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.helper.HeapMonitor;

import java.util.List;

public class Hypo3CompareStringConcatenationMain {
    private static final String DATA_DIRECTORY = "src/results/";
    private static final String RESULTS_FILENAME = DATA_DIRECTORY + "hypothesis3_concatenate_string.txt";
    private static final int NUMBER_OF_ITERATIONS = 1000;

    public static void main(String[] args) {
        System.out.println("Analyzing string concatenation performance...");

        System.out.println("Running efficient concatenation first...");
        //for (int i = 0; i < 5; i++)
        //runEfficientFirst();

        System.out.println("\nRunning inefficient concatenation first...");
        for (int i = 0; i < 5; i++)
        runInefficientFirst();
    }

    private static void runEfficientFirst() {
        // JVM warm-up and efficient concatenation
        prepareEnvironment(true, NUMBER_OF_ITERATIONS);
        HeapMonitor.startMonitoring();
        List<Double> efficientData = createData(true, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double efficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        HeapMonitor.resetPeakHeapUsage();

        // JVM warm-up and inefficient concatenation
        prepareEnvironment(false, NUMBER_OF_ITERATIONS);
        HeapMonitor.startMonitoring();
        List<Double> inefficientData = createData(false, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double inefficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        analyzeData(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage);
    }

    private static void runInefficientFirst() {
        // JVM warm-up and inefficient concatenation
        prepareEnvironment(false, NUMBER_OF_ITERATIONS);
        HeapMonitor.startMonitoring();
        List<Double> inefficientData = createData(false, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double inefficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        HeapMonitor.resetPeakHeapUsage();

        // JVM warm-up and efficient concatenation
        prepareEnvironment(true, NUMBER_OF_ITERATIONS);
        HeapMonitor.startMonitoring();
        List<Double> efficientData = createData(true, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double efficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        analyzeData(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage);
    }

    private static List<Double> createData(boolean isEfficient, int iterations) {
        Hypo3CompareStringConcatenation comparator = new Hypo3CompareStringConcatenation(isEfficient, iterations);
        return comparator.getExecutionTimes();
    }

    private static void prepareEnvironment(boolean isEfficient, int iterations) {
        new Hypo3CompareStringConcatenation(isEfficient, iterations);
    }

    private static void analyzeData(List<Double> efficientData, List<Double> inefficientData,
                                    double efficientPeakHeapUsage, double inefficientPeakHeapUsage) {
        FileDataManager fileManager = new FileDataManager(RESULTS_FILENAME);
        DataAnalyzer dataAnalyzer = new DataAnalyzer();

        dataAnalyzer.displayStatisticalAnalysis(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage, fileManager);
    }
}

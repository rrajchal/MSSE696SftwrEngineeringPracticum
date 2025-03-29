package org.msse696.optimization.main;

import org.msse696.optimization.compare.Hypo1ComparePadding;
import org.msse696.optimization.helper.DataAnalyzer;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.helper.HeapMonitor;

import java.util.List;

public class Hypo1ComparePaddingMain {

    private static final String DATA_DIRECTORY = "src/data/";
    private static final String EFFICIENT_DATA_FILE = DATA_DIRECTORY + "compare_padding_efficient.txt";
    private static final String INEFFICIENT_DATA_FILE = DATA_DIRECTORY + "compare_padding_inefficient.txt";
    private static final int NUMBER_OF_DATA_POINTS = 10_000_000;

    public static void main(String[] args) {
        System.out.println("Analyzing time taken to run...");

        //runEfficientFirstThenInefficient();
        runInefficientFirstThenEfficient();

    }
    private static void runEfficientFirstThenInefficient() {
        prepareEnvironmentForDataCreation(true, EFFICIENT_DATA_FILE, NUMBER_OF_DATA_POINTS);
        // Memory usage for efficient dataset
        HeapMonitor.resetPeakHeapUsage();
        HeapMonitor.startMonitoring();
        createData(true, EFFICIENT_DATA_FILE, NUMBER_OF_DATA_POINTS);
        HeapMonitor.stopMonitoring();
        double efficientPeak = HeapMonitor.getPeakHeapUsage();
        //System.out.printf("Peak Memory Used for Efficient Dataset: %.2f MB%n", efficientPeak);

        // Memory usage for inefficient dataset
        HeapMonitor.resetPeakHeapUsage();
        prepareEnvironmentForDataCreation(false, EFFICIENT_DATA_FILE, NUMBER_OF_DATA_POINTS);

        HeapMonitor.startMonitoring();
        createData(false, INEFFICIENT_DATA_FILE, NUMBER_OF_DATA_POINTS);
        HeapMonitor.stopMonitoring();
        double inefficientPeak = HeapMonitor.getPeakHeapUsage();
        //System.out.printf("Peak Memory Used for Inefficient Dataset: %.2f MB%n", inefficientPeak);

        // Statistical analysis
        analyzeData(efficientPeak, inefficientPeak);
    }

    private static void runInefficientFirstThenEfficient() {
        prepareEnvironmentForDataCreation(false, INEFFICIENT_DATA_FILE, NUMBER_OF_DATA_POINTS);
        // Memory usage for efficient dataset
        HeapMonitor.resetPeakHeapUsage();
        HeapMonitor.startMonitoring();
        createData(false, INEFFICIENT_DATA_FILE, NUMBER_OF_DATA_POINTS);
        HeapMonitor.stopMonitoring();
        double efficientPeak = HeapMonitor.getPeakHeapUsage();
        //System.out.printf("Peak Memory Used for Efficient Dataset: %.2f MB%n", efficientPeak);

        // Memory usage for inefficient dataset
        HeapMonitor.resetPeakHeapUsage();

        prepareEnvironmentForDataCreation(true, EFFICIENT_DATA_FILE, NUMBER_OF_DATA_POINTS);

        HeapMonitor.startMonitoring();
        createData(true, EFFICIENT_DATA_FILE, NUMBER_OF_DATA_POINTS);
        HeapMonitor.stopMonitoring();
        double inefficientPeak = HeapMonitor.getPeakHeapUsage();
        //System.out.printf("Peak Memory Used for Inefficient Dataset: %.2f MB%n", inefficientPeak);

        // Statistical analysis
        analyzeData(efficientPeak, inefficientPeak);
    }



    private static void analyzeData(double efficientPeak, double inefficientPeak) {
        DataAnalyzer dataAnalyzer = new DataAnalyzer();

        // Read data from the specified files
        List<Double> dataFromEfficientMethod = dataAnalyzer.readDataFromFile(EFFICIENT_DATA_FILE);
        List<Double> dataFromInefficientMethod = dataAnalyzer.readDataFromFile(INEFFICIENT_DATA_FILE);

        // Display statistical analysis for both datasets
        dataAnalyzer.displayStatisticalAnalysis(dataFromEfficientMethod, dataFromInefficientMethod, efficientPeak, inefficientPeak);
    }

    private static void createData(boolean isEfficient, String filePath, int numberOfIterations) {
        // Use FileDataManager to clear the file before writing new data
        FileDataManager fileDataManager = new FileDataManager(filePath);
        fileDataManager.clearData();
        new Hypo1ComparePadding(isEfficient, filePath, numberOfIterations);
    }

    /**
     * Prepares the environment for data creation by clearing existing data.
     * This step is similar to createData() but does not measure performance.
     * It generates the same amount of data and clears it afterward.
     *
     * JVM warm-up effects, such as Just-In-Time (JIT) compilation and adaptive optimization,
     * can impact performance during early execution. Clearing and recreating data ensures
     * consistent measurements by capturing execution times in a stable and optimized JVM state.
     *
     * Performing this step before monitoring ensures reliable and unbiased data for benchmarking.
     */
    private static void prepareEnvironmentForDataCreation(boolean isEfficient, String filePath, int numberOfIterations) {
        FileDataManager fileDataManager = new FileDataManager(filePath);
        new Hypo1ComparePadding(isEfficient, filePath, numberOfIterations);
        fileDataManager.clearData();
    }

}

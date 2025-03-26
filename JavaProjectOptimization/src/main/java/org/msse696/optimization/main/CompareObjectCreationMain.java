package org.msse696.optimization.main;

import org.msse696.optimization.compare.CompareObjectCreation;
import org.msse696.optimization.helper.DataAnalyzer;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.helper.HeapMonitor;

import java.util.List;

public class CompareObjectCreationMain {

    private static final String DATA_DIRECTORY = "src/data/";
    private static final String EFFICIENT_DATA_FILE = DATA_DIRECTORY + "object_creation_efficient.txt";
    private static final String INEFFICIENT_DATA_FILE = DATA_DIRECTORY + "object_creation_inefficient.txt";
    private static final int NUMBER_OF_ITERATIONS = 10000;

    public static void main(String[] args) {
        System.out.println("Analyzing object creation performance...");
        prepareEnvironmentForDataCreation(true, EFFICIENT_DATA_FILE, NUMBER_OF_ITERATIONS);
        // Memory usage for efficient object creation
        HeapMonitor.resetPeakHeapUsage();
        HeapMonitor.startMonitoring();
        createData(true, EFFICIENT_DATA_FILE, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double efficientPeak = HeapMonitor.getPeakHeapUsage();

        HeapMonitor.resetPeakHeapUsage();

        // Memory usage for inefficient object creation
        prepareEnvironmentForDataCreation(false, INEFFICIENT_DATA_FILE, NUMBER_OF_ITERATIONS);
        HeapMonitor.startMonitoring();
        createData(false, INEFFICIENT_DATA_FILE, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double inefficientPeak = HeapMonitor.getPeakHeapUsage();

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
        new CompareObjectCreation(isEfficient, filePath, numberOfIterations);
    }

    /**
     * Prepares the environment for data creation by clearing existing data.
     * Generates the same amount of data and clears it afterward.
     * This step avoids JVM warm-up effects, ensuring consistent and reliable benchmarks.
     */
    private static void prepareEnvironmentForDataCreation(boolean isEfficient, String filePath, int numberOfIterations) {
        FileDataManager fileDataManager = new FileDataManager(filePath);
        new CompareObjectCreation(isEfficient, filePath, numberOfIterations);
        fileDataManager.clearData();
    }
}

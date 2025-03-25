package org.msse696.optimization.main;

import org.msse696.optimization.compare.ComparePadding;
import org.msse696.optimization.helper.DataAnalyzer;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.helper.HeapMonitor;

import java.util.List;

public class ComparePaddingMain {

    private static final String DATA_DIRECTORY = "src/data/";
    private static final String EFFICIENT_DATA_FILE = DATA_DIRECTORY + "compare_padding_efficient.txt";
    private static final String INEFFICIENT_DATA_FILE = DATA_DIRECTORY + "compare_padding_inefficient.txt";
    private static final int NUMBER_OF_DATA_POINTS = 10000;

    public static void main(String[] args) {
        System.out.println("Analyzing time taken to run...");

        // Memory usage for efficient dataset
        HeapMonitor.resetPeakHeapUsage();
        HeapMonitor.startMonitoring();
        createData(true, EFFICIENT_DATA_FILE, NUMBER_OF_DATA_POINTS);
        HeapMonitor.stopMonitoring();
        double efficientPeak = HeapMonitor.getPeakHeapUsage();
        //System.out.printf("Peak Memory Used for Efficient Dataset: %.2f MB%n", efficientPeak);

        // Memory usage for inefficient dataset
        HeapMonitor.resetPeakHeapUsage();
        HeapMonitor.startMonitoring();
        createData(false, INEFFICIENT_DATA_FILE, NUMBER_OF_DATA_POINTS);
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
        new ComparePadding(isEfficient, filePath, numberOfIterations);
    }
}

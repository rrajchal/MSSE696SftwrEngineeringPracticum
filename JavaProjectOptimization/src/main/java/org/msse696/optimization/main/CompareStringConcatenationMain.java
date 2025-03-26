package org.msse696.optimization.main;

import org.msse696.optimization.compare.CompareStringConcatenation;
import org.msse696.optimization.helper.DataAnalyzer;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.helper.HeapMonitor;

import java.util.List;

public class CompareStringConcatenationMain {
    private static final String DATA_DIRECTORY = "src/data/";
    private static final String EFFICIENT_DATA_FILE = DATA_DIRECTORY + "string_concat_efficient.txt";
    private static final String INEFFICIENT_DATA_FILE = DATA_DIRECTORY + "string_concat_inefficient.txt";
    private static final int NUMBER_OF_ITERATIONS = 10000;

    public static void main(String[] args) {
        System.out.println("Analyzing string concatenation performance...");
        
        prepareEnvironment(true, EFFICIENT_DATA_FILE, NUMBER_OF_ITERATIONS);
        HeapMonitor.startMonitoring();
        createData(true, EFFICIENT_DATA_FILE, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double efficientPeak = HeapMonitor.getPeakHeapUsage();

        HeapMonitor.resetPeakHeapUsage();

        prepareEnvironment(false, INEFFICIENT_DATA_FILE, NUMBER_OF_ITERATIONS);
        HeapMonitor.startMonitoring();
        createData(false, INEFFICIENT_DATA_FILE, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double inefficientPeak = HeapMonitor.getPeakHeapUsage();

        analyzeData(efficientPeak, inefficientPeak);
    }

    private static void analyzeData(double efficientPeak, double inefficientPeak) {
        DataAnalyzer dataAnalyzer = new DataAnalyzer();
        List<Double> efficientData = dataAnalyzer.readDataFromFile(EFFICIENT_DATA_FILE);
        List<Double> inefficientData = dataAnalyzer.readDataFromFile(INEFFICIENT_DATA_FILE);
        dataAnalyzer.displayStatisticalAnalysis(efficientData, inefficientData, efficientPeak, inefficientPeak);
    }

    private static void createData(boolean isEfficient, String filePath, int iterations) {
        FileDataManager fileDataManager = new FileDataManager(filePath);
        fileDataManager.clearData();
        new CompareStringConcatenation(isEfficient, filePath, iterations);
    }

    private static void prepareEnvironment(boolean isEfficient, String filePath, int iterations) {
        FileDataManager fileDataManager = new FileDataManager(filePath);
        new CompareStringConcatenation(isEfficient, filePath, iterations);
        fileDataManager.clearData();
    }
}

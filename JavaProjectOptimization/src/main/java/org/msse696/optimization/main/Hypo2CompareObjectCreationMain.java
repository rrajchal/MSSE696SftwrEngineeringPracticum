package org.msse696.optimization.main;

import org.msse696.optimization.compare.Hypo2CompareObjectCreation;
import org.msse696.optimization.helper.DataAnalyzer;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.helper.HeapMonitor;

import java.util.List;

public class Hypo2CompareObjectCreationMain {
    private static final int NUMBER_OF_ITERATIONS = 1000;
    private static final String RESULTS_DIRECTORY = "src/results/";
    private static final String RESULTS_FILENAME = RESULTS_DIRECTORY + "hypothesis2_object_creation_efficiency.txt";

    public static void main(String[] args) {
        System.out.println("Analyzing object creation performance...");

        System.out.println("Running efficient first, then inefficient...");
        //for (int i = 0; i < 5; i++)
            //runEfficientFirstThenInefficient();


        System.out.println("Running inefficient first, then efficient...");
        //for (int i = 0; i < 5; i++)
            runInefficientFirstThenEfficient();

    }

    private static void runEfficientFirstThenInefficient() {
        // JVM warm-up with efficient creation
        createData(true, NUMBER_OF_ITERATIONS);

        // Efficient object creation
        HeapMonitor.startMonitoring();
        List<Double> efficientData = createData(true, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double efficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        HeapMonitor.resetPeakHeapUsage();

        // JVM warm-up with inefficient creation
        createData(false, NUMBER_OF_ITERATIONS);

        // Inefficient object creation
        HeapMonitor.startMonitoring();
        List<Double> inefficientData = createData(false, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double inefficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        analyzeData(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage);
    }

    private static void runInefficientFirstThenEfficient() {
        // JVM warm-up with inefficient creation
        createData(false, NUMBER_OF_ITERATIONS);

        // Inefficient object creation
        HeapMonitor.startMonitoring();
        List<Double> inefficientData = createData(false, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double inefficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        HeapMonitor.resetPeakHeapUsage();

        // JVM warm-up with efficient creation
        createData(true, NUMBER_OF_ITERATIONS);

        // Efficient object creation
        HeapMonitor.startMonitoring();
        List<Double> efficientData = createData(true, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double efficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        analyzeData(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage);
    }

    private static List<Double> createData(boolean isEfficient, int iterations) {
        Hypo2CompareObjectCreation comparator = new Hypo2CompareObjectCreation(isEfficient, iterations);
        return comparator.getExecutionTimes();
    }

    private static void analyzeData(List<Double> efficientData, List<Double> inefficientData,
                                    double efficientPeakHeapUsage, double inefficientPeakHeapUsage) {
        FileDataManager fileManager = new FileDataManager(RESULTS_FILENAME);
        DataAnalyzer dataAnalyzer = new DataAnalyzer();

        dataAnalyzer.displayStatisticalAnalysis(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage, fileManager);
    }
}

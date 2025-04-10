package org.msse696.optimization.main;

import org.msse696.optimization.compare.Hypo5CompareCommonExpressions;
import org.msse696.optimization.helper.DataAnalyzer;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.helper.HeapMonitor;
import org.msse696.optimization.helper.debug.Debug;

import java.util.List;

public class Hypo5CompareCommonExpressionsMain {
    private static final int NUMBER_OF_ITERATIONS = 10_000_000;
    private static final String RESULTS_DIRECTORY = "src/results/";
    private static final String RESULTS_FILENAME = RESULTS_DIRECTORY + "hypothesis5_common_expression.txt";

    public static void main(String[] args) {
        Debug.info("Analyzing common expression elimination performance...");

        Debug.info("\nRunning efficient first, then inefficient...");
        //for (int i = 0; i < 5; i++)
        //    runEfficientFirstThenInefficient();

        Debug.info("Running inefficient first, then efficient...");
        for (int i = 0; i < 5; i++)
            runInefficientFirstThenEfficient();
    }

    private static void runEfficientFirstThenInefficient() {

        createData(true, NUMBER_OF_ITERATIONS);
        // Efficient calculation
        HeapMonitor.startMonitoring();
        List<Double> efficientData = createData(true, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double efficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        HeapMonitor.resetPeakHeapUsage();

        createData(false, NUMBER_OF_ITERATIONS);
        // Inefficient calculation
        HeapMonitor.startMonitoring();
        List<Double> inefficientData = createData(false, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double inefficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        analyzeData(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage);
    }

    private static void runInefficientFirstThenEfficient() {
        createData(false, NUMBER_OF_ITERATIONS);
        // Inefficient calculation
        HeapMonitor.startMonitoring();
        List<Double> inefficientData = createData(false, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double inefficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        HeapMonitor.resetPeakHeapUsage();

        createData(true, NUMBER_OF_ITERATIONS);
        // Efficient calculation
        HeapMonitor.startMonitoring();
        List<Double> efficientData = createData(true, NUMBER_OF_ITERATIONS);
        HeapMonitor.stopMonitoring();
        double efficientPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        analyzeData(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage);
    }

    private static List<Double> createData(boolean isEfficient, int iterations) {
        Hypo5CompareCommonExpressions comparator = new Hypo5CompareCommonExpressions(isEfficient, iterations);
        return comparator.getExecutionTimes();
    }

    private static void analyzeData(List<Double> efficientData, List<Double> inefficientData,
                                    double efficientPeakHeapUsage, double inefficientPeakHeapUsage) {
        FileDataManager fileManager = new FileDataManager(RESULTS_FILENAME);
        DataAnalyzer dataAnalyzer = new DataAnalyzer();
        dataAnalyzer.displayStatisticalAnalysis(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage, fileManager);
    }
}

package org.msse696.optimization.main;

import org.msse696.optimization.compare.Hypo1CompareVariableDeclarationOrderPerformance;
import org.msse696.optimization.helper.DataAnalyzer;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.helper.HeapMonitor;
import org.msse696.optimization.helper.debug.Debug;

import java.util.List;

public class Hypo1CompareVariableDeclarationOrderMain {
    private static final int OBJECT_COUNT = 1_000_000; // Number of objects to create
    private static final String RESULTS_DIRECTORY = "src/results/";
    private static final String RESULTS_FILENAME = RESULTS_DIRECTORY + "hypothesis1_variable_order_efficiency.txt";

    public static void main(String[] args) {
        Debug.info("Analyzing variable declaration order performance...");

        Debug.info("Running optimized first, then unoptimized...");
        for (int i = 0; i < 5; i++)
            runOptimizedFirstThenUnoptimized();

        Debug.info("\nRunning unoptimized first, then optimized...");
        for (int i = 0; i < 5; i++)
            runUnoptimizedFirstThenOptimized();
    }

    private static void runOptimizedFirstThenUnoptimized() {
        createData(true, OBJECT_COUNT);
        // Optimized objects creation
        HeapMonitor.startMonitoring();
        List<Double> optimizedData = createData(true, OBJECT_COUNT);
        HeapMonitor.stopMonitoring();
        double optimizedPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        HeapMonitor.resetPeakHeapUsage();

        createData(false, OBJECT_COUNT);
        // Unoptimized objects creation
        HeapMonitor.startMonitoring();
        List<Double> unoptimizedData = createData(false, OBJECT_COUNT);
        HeapMonitor.stopMonitoring();
        double unoptimizedPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        analyzeData(optimizedData, unoptimizedData, optimizedPeakHeapUsage, unoptimizedPeakHeapUsage);
    }

    private static void runUnoptimizedFirstThenOptimized() {
        // Unoptimized objects creation
        HeapMonitor.startMonitoring();
        List<Double> unoptimizedData = createData(false, OBJECT_COUNT);
        HeapMonitor.stopMonitoring();
        double unoptimizedPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        HeapMonitor.resetPeakHeapUsage();

        // Optimized objects creation
        HeapMonitor.startMonitoring();
        List<Double> optimizedData = createData(true, OBJECT_COUNT);
        HeapMonitor.stopMonitoring();
        double optimizedPeakHeapUsage = HeapMonitor.getPeakHeapUsage();

        analyzeData(optimizedData, unoptimizedData, optimizedPeakHeapUsage, unoptimizedPeakHeapUsage);
    }

    private static List<Double> createData(boolean isOptimized, int count) {
        Hypo1CompareVariableDeclarationOrderPerformance comparator = new Hypo1CompareVariableDeclarationOrderPerformance(isOptimized, count);
        return comparator.getExecutionTimes();
    }

    private static void analyzeData(List<Double> efficientData, List<Double> inefficientData,
                                     double efficientPeakHeapUsage, double inefficientPeakHeapUsage) {
        FileDataManager fileManager = new FileDataManager(RESULTS_FILENAME);
        DataAnalyzer dataAnalyzer = new DataAnalyzer();
        dataAnalyzer.displayStatisticalAnalysis(efficientData, inefficientData, efficientPeakHeapUsage, inefficientPeakHeapUsage, fileManager);
    }
}

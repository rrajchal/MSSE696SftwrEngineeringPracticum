package org.msse696.optimization.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataAnalyzer {

    private static final double DEFAULT_SIGNIFICANCE_LEVEL = 0.01; // Constant for default significance level
    private static final double CRITICAL_VALUE_95 = 1.96;          // Critical value for 95% confidence
    private static final double CRITICAL_VALUE_99 = 2.576;         // Critical value for 99% confidence

    // Reads data from the specified file
    public List<Double> readDataFromFile(String filePath) {
        List<Double> data = new ArrayList<>();
        File file = new File(filePath);

        if (!file.exists()) {
            System.err.println("File not found: " + filePath);
            return data;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    data.add(Double.parseDouble(line.trim())); // Parse numbers
                } catch (NumberFormatException e) {
                    System.err.println("Skipping invalid number: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return data;
    }

    // Calculates the average of a dataset
    public double calculateAverage(List<Double> data) {
        if (data.isEmpty()) return 0;
        double sum = 0;
        for (double value : data) {
            sum += value;
        }
        return sum / data.size();
    }

    // Calculates the standard deviation of a dataset
    public double calculateStandardDeviation(List<Double> data) {
        if (data.isEmpty()) return 0;
        double mean = calculateAverage(data);
        double sumOfSquares = 0;
        for (double value : data) {
            sumOfSquares += Math.pow(value - mean, 2);
        }
        return Math.sqrt(sumOfSquares / data.size());
    }

    // Determines if two datasets are significantly different
    public boolean isSignificantlyDifferent(List<Double> efficientData, List<Double> inefficientData, double significanceLevel) {
        if (efficientData.isEmpty() || inefficientData.isEmpty()) {
            System.err.println("Error: One or both datasets are empty.");
            return false;
        }

        if (efficientData.size() <= 1 || inefficientData.size() <= 1) {
            System.err.println("Error: One or both datasets have insufficient data points for statistical analysis.");
            return false;
        }

        double mean1 = calculateAverage(efficientData);
        double mean2 = calculateAverage(inefficientData);
        double stdDev1 = calculateStandardDeviation(efficientData);
        double stdDev2 = calculateStandardDeviation(inefficientData);

        if (stdDev1 == 0 || stdDev2 == 0) {
            System.err.println("Error: One or both datasets have zero variance. Statistical comparison is invalid.");
            return false;
        }

        int n1 = efficientData.size();
        int n2 = inefficientData.size();
        double pooledStdDev = Math.sqrt(((n1 - 1) * Math.pow(stdDev1, 2) + (n2 - 1) * Math.pow(stdDev2, 2)) / (n1 + n2 - 2));
        double tStatistic = (mean1 - mean2) / (pooledStdDev * Math.sqrt((1.0 / n1) + (1.0 / n2)));

        double criticalValue = significanceLevel == DEFAULT_SIGNIFICANCE_LEVEL ? CRITICAL_VALUE_99 : CRITICAL_VALUE_95;

        return Math.abs(tStatistic) > criticalValue;
    }

    public boolean isSignificantlyDifferent(List<Double> efficientData, List<Double> inefficientData) {
        return isSignificantlyDifferent(efficientData, inefficientData, DEFAULT_SIGNIFICANCE_LEVEL);
    }

    public void displayStatisticalAnalysis(List<Double> efficientData, List<Double> inefficientData, double efficientPeakMem, double inefficientPeakMem) {
        System.out.println("Statistical Analysis:");
        System.out.println("----------------------------------------------------------------");
        System.out.printf("| %-29s | %-11s | %-14s |\n", "Metric", "Dataset 1", "Dataset 2");
        System.out.printf("| %-29s | %-11s | %-14s |\n", "", "(Efficient)", "(Inefficient)");
        System.out.println("----------------------------------------------------------------");
        System.out.printf("| Number of Data Points         | %-11d | %-14d |\n", efficientData.size(), inefficientData.size());
        printRow("Average", calculateAverage(efficientData), calculateAverage(inefficientData));
        printRow("Standard Deviation", calculateStandardDeviation(efficientData), calculateStandardDeviation(inefficientData));
        printRow("Peak Memory (MB)", efficientPeakMem, inefficientPeakMem);

        System.out.println("----------------------------------------------------------------");

        if (isSignificantlyDifferent(efficientData, inefficientData)) {
            System.out.println("The difference between Dataset 1 and Dataset 2 is statistically **significant** at the 1.0% level.");
        } else {
            System.out.println("The difference between Dataset 1 and Dataset 2 is statistically **insignificant** at the 1.0% level.");
        }
    }

    public void displayStatisticalAnalysis(List<Double> efficientData, List<Double> inefficientData) {
        displayStatisticalAnalysis(efficientData, inefficientData, 0.0, 0.0);
    }

    // Prints a formatted row in the statistical analysis table
    private void printRow(String metric, double efficientValue, double inefficientValue) {
        System.out.printf("| %-29s | %-11.3f | %-14.3f |\n", metric, efficientValue, inefficientValue);
    }
}
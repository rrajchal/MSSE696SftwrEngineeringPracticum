package org.msse696.optimization.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataAnalyzer {

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

    // Method to calculate the average of a dataset
    public double calculateAverage(List<Double> data) {
        if (data.isEmpty()) return 0;
        double sum = 0;
        for (double value : data) {
            sum += value;
        }
        return sum / data.size();
    }

    // Method to calculate the standard deviation of a dataset
    public double calculateStandardDeviation(List<Double> data) {
        if (data.isEmpty()) return 0;
        double mean = calculateAverage(data);
        double sumOfSquares = 0;
        for (double value : data) {
            sumOfSquares += Math.pow(value - mean, 2);
        }
        return Math.sqrt(sumOfSquares / data.size());
    }

    // Method to perform a t-test to determine if two datasets are significantly different
    public boolean isSignificantlyDifferent(List<Double> data1, List<Double> data2, double significanceLevel) {
        if (data1.isEmpty() || data2.isEmpty()) {
            System.err.println("One or both datasets are empty");
            return false;
        }

        double mean1 = calculateAverage(data1);
        double mean2 = calculateAverage(data2);
        double stdDev1 = calculateStandardDeviation(data1);
        double stdDev2 = calculateStandardDeviation(data2);

        // Calculate pooled standard deviation
        int n1 = data1.size();
        int n2 = data2.size();
        double pooledStdDev = Math.sqrt(((n1 - 1) * Math.pow(stdDev1, 2) + (n2 - 1) * Math.pow(stdDev2, 2)) / (n1 + n2 - 2));

        // Calculate t-statistic
        double tStatistic = (mean1 - mean2) / (pooledStdDev * Math.sqrt((1.0 / n1) + (1.0 / n2)));

        // For simplicity, we'll approximate the critical t-value for a two-tailed test with large degrees of freedom
        double criticalValue = 1.96; // Corresponds to ~95% confidence level for two-tailed test
        if (significanceLevel == 0.01) criticalValue = 2.576; // ~99% confidence level

        return Math.abs(tStatistic) > criticalValue;
    }
}

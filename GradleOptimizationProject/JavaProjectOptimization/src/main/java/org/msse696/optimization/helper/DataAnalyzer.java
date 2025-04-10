package org.msse696.optimization.helper;

import org.msse696.optimization.helper.debug.Debug;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * DataAnalyzer provides methods for statistical analysis of two datasets.
 * It calculates metrics like average, standard deviation, and checks
 * whether datasets are significantly different using a t-test.
 */
public class DataAnalyzer {

    // Constants for significance level and critical values
    private static final double DEFAULT_SIGNIFICANCE_LEVEL = 0.01; // Constant for default significance level
    private static final double CRITICAL_VALUE_95 = 1.96;          // Critical value for 95% confidence
    private static final double CRITICAL_VALUE_99 = 2.576;         // Critical value for 99% confidence

    /**
     * Reads numerical data from a file line-by-line and converts it to a list of doubles.
     *
     * @param filePath Path of the file containing numerical data.
     * @return A List of Double values representing the dataset.
     */
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

    /**
     * Calculates the standard deviation of a dataset.
     *
     * @param data The dataset as a List of Double values.
     * @return The standard deviation or 0 if the dataset is empty.
     */
    public double calculateStandardDeviation(List<Double> data) {
        if (data.isEmpty()) return 0;
        double mean = calculateAverage(data);
        double sumOfSquares = 0;
        for (double value : data) {
            sumOfSquares += Math.pow(value - mean, 2);
        }
        return Math.sqrt(sumOfSquares / data.size());
    }

    /**
     * Determines whether two datasets are significantly different using a t-test.
     *
     * @param efficientData   Dataset 1 (efficient implementation).
     * @param inefficientData Dataset 2 (inefficient implementation).
     * @param significanceLevel The significance level to use for the t-test.
     * @return True if the datasets are significantly different, otherwise false.
     */
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

    /**
     * Overloaded method that uses the default significance level of 1%.
     *
     * @param efficientData   Dataset 1 (efficient implementation).
     * @param inefficientData Dataset 2 (inefficient implementation).
     * @return True if the datasets are significantly different, otherwise false.
     */
    public boolean isSignificantlyDifferent(List<Double> efficientData, List<Double> inefficientData) {
        return isSignificantlyDifferent(efficientData, inefficientData, DEFAULT_SIGNIFICANCE_LEVEL);
    }

    /**
     * Displays the statistical analysis of two datasets, including averages,
     * standard deviations, and whether the difference is statistically significant.
     *
     * @param efficientData   Dataset 1 (efficient implementation).
     * @param inefficientData Dataset 2 (inefficient implementation).
     * @param efficientPeakMem Peak memory usage of Dataset 1.
     * @param inefficientPeakMem Peak memory usage of Dataset 2.
     */
    public String displayStatisticalAnalysis(List<Double> efficientData, List<Double> inefficientData, double efficientPeakMem, double inefficientPeakMem, FileDataManager fileManager) {
        StringBuilder analysisResult = new StringBuilder();

        analysisResult.append("Statistical Analysis:\n");
        analysisResult.append("----------------------------------------------------------------\n");
        analysisResult.append(String.format("| %-29s | %-11s | %-14s |\n", "Metric", "Dataset 1", "Dataset 2"));
        analysisResult.append(String.format("| %-29s | %-11s | %-14s |\n", "", "(Efficient)", "(Inefficient)"));
        analysisResult.append("----------------------------------------------------------------\n");
        analysisResult.append(String.format("| Number of Data Points         | %-11d | %-14d |\n", efficientData.size(), inefficientData.size()));
        analysisResult.append(getRow("Average (nanoseconds)", calculateAverage(efficientData), calculateAverage(inefficientData)));
        analysisResult.append(getRow("Standard Deviation (ns)", calculateStandardDeviation(efficientData), calculateStandardDeviation(inefficientData)));
        analysisResult.append(getRow("Peak Memory (MB)", efficientPeakMem, inefficientPeakMem));
        analysisResult.append("----------------------------------------------------------------\n");

        if (isSignificantlyDifferent(efficientData, inefficientData)) {
            analysisResult.append("The difference between Dataset 1 and Dataset 2 is statistically **significant** at the 1.0% level.\n");
        } else {
            analysisResult.append("The difference between Dataset 1 and Dataset 2 is statistically **insignificant** at the 1.0% level.\n");
        }

        // Print the analysis result
        Debug.info(analysisResult.toString());

        // Save the analysis to the file
        fileManager.appendLine(analysisResult.toString());

        return analysisResult.toString();
    }

    public void displayStatisticalAnalysis(List<Double> efficientData, List<Double> inefficientData, double efficientPeakMem, double inefficientPeakMem) {
        Debug.info("Statistical Analysis:");
        Debug.info("----------------------------------------------------------------");
        System.out.printf("| %-29s | %-11s | %-14s |\n", "Metric", "Dataset 1", "Dataset 2");
        System.out.printf("| %-29s | %-11s | %-14s |\n", "", "(Efficient)", "(Inefficient)");
        Debug.info("----------------------------------------------------------------");
        System.out.printf("| Number of Data Points         | %-11d | %-14d |\n", efficientData.size(), inefficientData.size());
        printRow("Average (nanoseconds)", calculateAverage(efficientData), calculateAverage(inefficientData));
        printRow("Standard Deviation", calculateStandardDeviation(efficientData), calculateStandardDeviation(inefficientData));
        printRow("Peak Memory (MB)", efficientPeakMem, inefficientPeakMem);

        Debug.info("----------------------------------------------------------------");

        if (isSignificantlyDifferent(efficientData, inefficientData)) {
            Debug.info("The difference between Dataset 1 and Dataset 2 is statistically **significant** at the 1.0% level.");
        } else {
            Debug.info("The difference between Dataset 1 and Dataset 2 is statistically **insignificant** at the 1.0% level.");
        }
    }

    public void displayStatisticalAnalysis(List<Double> efficientData, List<Double> inefficientData) {
        displayStatisticalAnalysis(efficientData, inefficientData, 0.0, 0.0);
    }

    // Prints a formatted row in the statistical analysis table
    private void printRow(String metric, double efficientValue, double inefficientValue) {
        System.out.printf("| %-29s | %-11.3f | %-14.3f |\n", metric, efficientValue, inefficientValue);
    }

    private String getRow(String metric, double dataset1Value, double dataset2Value) {
        return String.format("| %-29s | %-11.3f | %-14.3f |\n", metric, dataset1Value, dataset2Value);
    }
}
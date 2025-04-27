package org.msse696;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ReportAggregator {
    static Map<String, Integer> allAnalyzerFrequency = new HashMap<>();
    /**
     * Main method to search directories, analyze reports, and generate a CSV.
     */
    public static void main(String[] args) {
        // Hardcoded directory path to search for analysis_report.html files
        String rootDirectoryPath = "C:\\Users\\Rajesh\\Desktop\\JavaProjectDownloads\\Done";
        File rootDirectory = new File(rootDirectoryPath);

        if (!rootDirectory.exists() || !rootDirectory.isDirectory()) {
            System.out.println("The provided path is not a valid directory: " + rootDirectoryPath);
            return;
        }

        File csvOutputFile = new File("report.csv");
        try (FileWriter csvWriter = new FileWriter(csvOutputFile)) {
            csvWriter.append("Total Inefficiencies,Total Lines of Code,Total Java Files,Most Frequent Analyzer,Second Most Frequent Analyzer,Third Most Frequent Analyzer\n");

            // Recursively process directories
            processDirectory(rootDirectory, csvWriter);

            System.out.println("Report aggregation completed. Output written to report.csv: " + csvOutputFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error while writing the CSV file: " + e.getMessage());
        }
        // Display Map Content : Aggregate into allAnalyzerFrequency
        for (Map.Entry<String, Integer> entry : allAnalyzerFrequency.entrySet()) {
            System.out.println(entry.getKey() + "->" + entry.getValue());
        }
    }

    /**
     * Processes a directory recursively and analyzes any analysis_report.html files found.
     * @param directory The directory to process.
     * @param csvWriter The CSV file writer.
     * @throws IOException If an error occurs during file processing.
     */
    private static void processDirectory(File directory, FileWriter csvWriter) throws IOException {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                // Recurse into subdirectories
                processDirectory(file, csvWriter);
            } else if (file.getName().equals("analysis_report.html")) {
                // Analyze the report and write to the CSV
                analyzeReport(file, csvWriter);
            }
        }
    }

    /**
     * Analyzes the contents of an analysis_report.html file and writes data to the CSV.
     * @param reportFile The analysis_report.html file to analyze.
     * @param csvWriter The CSV file writer.
     * @throws IOException If an error occurs during file processing.
     */
    private static void analyzeReport(File reportFile, FileWriter csvWriter) throws IOException {
        int totalInefficiencies = 0;
        int totalLinesOfCode = 0;
        int totalJavaFiles = 0;
        Map<String, Integer> analyzerFrequency = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(reportFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Extract specific data from HTML lines
                if (line.contains("Total Inefficiencies Detected Across All Classes:")) {
                    totalInefficiencies = extractNumberFromHtml(line);
                } else if (line.contains("Total # of lines of code Across All Classes:")) {
                    totalLinesOfCode = extractNumberFromHtml(line);
                } else if (line.contains("Total Java Files Analyzed:")) {
                    totalJavaFiles = extractNumberFromHtml(line);
                }

                // Count occurrences of analyzers
                for (String analyzer : new String[]{
                        "ArithmeticOperationAnalyzer",
                        "ArrayCopyInefficiencyAnalyzer",
                        "CastingAnalyzer",
                        "ExpressionEliminationAnalyzer",
                        "InstanceofAnalyzer",
                        "LoopInefficiencyAnalyzer",
                        "LoopObjectCreationAnalyzer",
                        "PaddingAnalyzer",
                        "StringConcatenationAnalyzer",
                        "TryCatchAnalyzer"}) {
                    if (line.contains(analyzer)) {
                        analyzerFrequency.put(analyzer, analyzerFrequency.getOrDefault(analyzer, 0) + 1);
                        allAnalyzerFrequency.put(analyzer, analyzerFrequency.getOrDefault(analyzer, 0) + 1);
                    } else {
                        analyzerFrequency.put("None", analyzerFrequency.getOrDefault("None", 0) + 1);
                        allAnalyzerFrequency.put("None", analyzerFrequency.getOrDefault("None", 0) + 1);
                    }
                }
            }
        }

        // Get the three most frequent analyzers
        List<String> topAnalyzers = analyzerFrequency.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Sort by frequency (descending)
                .map(Map.Entry::getKey) // Extract keys (analyzers)
                .toList(); // Collect into a list

        // Extract the first, second, and third most frequent analyzers
        String mostFrequentAnalyzer = !topAnalyzers.isEmpty() ? topAnalyzers.get(0) : "None";
        String secondMostFrequentAnalyzer = topAnalyzers.size() > 1 ? topAnalyzers.get(1) : "None";
        String thirdMostFrequentAnalyzer = topAnalyzers.size() > 2 ? topAnalyzers.get(2) : "None";



        // Write the extracted data and analyzers to the CSV
        csvWriter.append(String.valueOf(totalInefficiencies)).append(",").append(String.valueOf(totalLinesOfCode)).append(",").append(String.valueOf(totalJavaFiles)).append(",").append(mostFrequentAnalyzer).append(",").append(secondMostFrequentAnalyzer).append(",").append(thirdMostFrequentAnalyzer).append("\n");
    }

    /**
     * Helper method to extract the number from an HTML line that includes <strong> tags.
     * @param htmlLine The line containing HTML data.
     * @return The extracted number.
     */
    private static int extractNumberFromHtml(String htmlLine) {
        try {
            // Extract the number between <strong> tags
            int startIndex = htmlLine.indexOf("<strong>") + 8; // Start after <strong>
            int endIndex = htmlLine.indexOf("</strong>"); // End before </strong>
            String numberString = htmlLine.substring(startIndex, endIndex).trim();
            startIndex = numberString.indexOf(": ") + 1;
            endIndex = numberString.length();
            numberString = numberString.substring(startIndex, endIndex).trim();
            return Integer.parseInt(numberString); // Parse the number
        } catch (Exception e) {
            System.err.println("Error extracting number from line: " + htmlLine);
            return 0; // Default to 0 if parsing fails
        }
    }
}

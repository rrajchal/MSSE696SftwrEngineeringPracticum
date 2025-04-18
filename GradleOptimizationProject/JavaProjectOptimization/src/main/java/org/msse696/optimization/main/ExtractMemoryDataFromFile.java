package org.msse696.optimization.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractMemoryDataFromFile {

    public static void main(String[] args) {
        String filePath = "src/results/hypothesis7_type_checking_efficiency.txt"; // Replace with the actual path
        List<MemoryData> memoryDataList = readAndExtractMemory(filePath);
        if (memoryDataList != null && !memoryDataList.isEmpty()) {
            writeToCsv("src/results/peak_memory_from_file.csv", memoryDataList);
            calculateAndPrintAverage(memoryDataList);
            System.out.println("Data extracted, average calculated, and saved to peak_memory_from_file.csv");
        } else {
            System.err.println("Error reading data from " + filePath + " or no data found.");
        }
    }

    static class MemoryData {
        double efficientMemory;
        double inefficientMemory;
        String significance;

        public MemoryData(double efficientMemory, double inefficientMemory, String significance) {
            this.efficientMemory = efficientMemory;
            this.inefficientMemory = inefficientMemory;
            this.significance = significance;
        }

        @Override
        public String toString() {
            return efficientMemory + "," + inefficientMemory + "," + significance;
        }
    }

    public static List<MemoryData> readAndExtractMemory(String filePath) {
        List<MemoryData> memoryDataList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Pattern memoryPattern = Pattern.compile("\\| Peak Memory \\(MB\\)\\s*\\|\\s*(\\d+\\.\\d+)\\s*\\|\\s*(\\d+\\.\\d+)\\s*\\|");
            Pattern significancePattern = Pattern.compile("The difference between Dataset 1 and Dataset 2 is statistically \\*\\*(\\w+)\\*\\*");
            double efficientMemory = -1;
            double inefficientMemory = -1;
            String significance = "";

            while ((line = reader.readLine()) != null) {
                Matcher memoryMatcher = memoryPattern.matcher(line);
                if (memoryMatcher.find()) {
                    efficientMemory = Double.parseDouble(memoryMatcher.group(1));
                    inefficientMemory = Double.parseDouble(memoryMatcher.group(2));
                }

                Matcher significanceMatcher = significancePattern.matcher(line);
                if (significanceMatcher.find()) {
                    significance = significanceMatcher.group(1);
                    if (efficientMemory != -1 && inefficientMemory != -1) {
                        memoryDataList.add(new MemoryData(efficientMemory, inefficientMemory, significance));
                        efficientMemory = -1; // Reset for the next block
                        inefficientMemory = -1;
                        significance = "";
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return memoryDataList;
    }

    public static void writeToCsv(String filename, List<MemoryData> dataList) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write("Efficient_Peak_Memory(MB),Inefficient_Peak_Memory(MB),Statistical_Significance\n");
            for (MemoryData data : dataList) {
                writer.write(data.toString() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void calculateAndPrintAverage(List<MemoryData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            System.out.println("No data to calculate average.");
            return;
        }

        double sumEfficient = 0;
        double sumInefficient = 0;

        for (MemoryData data : dataList) {
            sumEfficient += data.efficientMemory;
            sumInefficient += data.inefficientMemory;
        }

        double averageEfficient = sumEfficient / dataList.size();
        double averageInefficient = sumInefficient / dataList.size();

        System.out.println("\nAverage Peak Memory:");
        System.out.printf("  Efficient: %.3f MB%n", averageEfficient);
        System.out.printf("  Inefficient: %.3f MB%n", averageInefficient);
    }
}
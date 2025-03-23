package org.msse696.optimization.helper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileDataManager {

    private final String filePath;
    private final String efficientFilePath = "efficient_data.txt";
    private final String inefficientFilePath = "inefficient_data.txt";

    public FileDataManager() {
        this.filePath = null;
    }

    // Constructor with default file path based on efficiency
    public FileDataManager(boolean isEfficient) {
        this.filePath = isEfficient ? efficientFilePath : inefficientFilePath;
    }

    // Constructor with a specified custom file path
    public FileDataManager(String filePath, boolean isEfficient) {
        if (filePath != null && !filePath.isEmpty()) {
            this.filePath = filePath;
        } else {
            this.filePath = isEfficient ? efficientFilePath : inefficientFilePath;
        }
    }

    // Method to append a string to the file
    public void appendLine(String line) {
        try {
            // Ensure file exists, create if not
            File file = new File(filePath);
            if (!file.exists()) {
                boolean isFileCreated = file.createNewFile();
                if (isFileCreated) {
                    System.out.println("File created: " + filePath);
                }
            }

            // Write the line to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    // Overloaded method to append an integer to the file
    public void appendLine(int line) {
        appendLine(String.valueOf(line));
    }

    // Overloaded method to append a long to the file
    public void appendLine(long line) {
        appendLine(String.valueOf(line));
    }

    // Method to retrieve a list of Double data from the file
    public List<Double> readData() {
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
                    data.add(Double.parseDouble(line.trim()));
                } catch (NumberFormatException e) {
                    System.err.println("Skipping invalid number: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return data;
    }

    // Method to clear all data in the file
    public void clearData() {
        try {
            // Overwrite the file with an empty content
            File file = new File(filePath);
            if (!file.exists()) {
                boolean isFileCreated = file.createNewFile();
                if (isFileCreated) {
                    System.out.println("File created: " + filePath);
                }
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                writer.write(""); // Clears the content of the file
            }
            System.out.println("File cleared: " + filePath);
        } catch (IOException e) {
            System.err.println("Error clearing file: " + e.getMessage());
        }
    }

    public void clearData(String singleFilePath) {
        try {
            // Overwrite the file with an empty content
            File file = new File(singleFilePath);
            if (!file.exists()) {
                boolean isFileCreated = file.createNewFile();
                if (isFileCreated) {
                    System.out.println("File created: " + singleFilePath);
                }
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                writer.write(""); // Clears the content of the file
            }
            System.out.println("File cleared: " + singleFilePath);
        } catch (IOException e) {
            System.err.println("Error clearing file: " + e.getMessage());
        }
    }

    public void clearBothData() {
        clearData(efficientFilePath);
        clearData(inefficientFilePath);
    }
}

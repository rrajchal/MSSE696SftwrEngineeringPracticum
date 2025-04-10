package org.msse696.optimization.helper;

import org.msse696.optimization.helper.debug.Debug;

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

    // Constructor requiring a file path
    public FileDataManager(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            throw new IllegalArgumentException("File path must not be null or empty.");
        }
        this.filePath = filePath;

        // Ensure the directory exists
        File file = new File(filePath);
        File parentDir = file.getParentFile(); // Get the parent directory
        if (parentDir != null && !parentDir.exists()) {
            boolean isDirCreated = parentDir.mkdirs();
            if (isDirCreated) {
                Debug.info("Directory created: " + parentDir.getAbsolutePath());
            }
        }
    }

    // Method to append a string to the file
    public void appendLine(String line) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                boolean isFileCreated = file.createNewFile();
                if (isFileCreated) {
                    Debug.info("File created: " + filePath);
                }
            }

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

    // Overloaded method to append a double to the file
    public void appendLine(double line) {
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
            File file = new File(filePath);
            if (!file.exists()) {
                boolean isFileCreated = file.createNewFile();
                if (isFileCreated) {
                    Debug.info("File created: " + filePath);
                }
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                writer.write(""); // Clears the content of the file
            }
            //Debug.info("File cleared: " + filePath);
        } catch (IOException e) {
            System.err.println("Error clearing file: " + e.getMessage());
        }
    }
}

package org.msse696.optimization.helper;

import org.msse696.optimization.helper.debug.Debug;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * JavaFileScanner is a utility class designed to scan a directory and its subdirectories
 * for Java files (*.java). It processes the directory upon instantiation and provides
 * methods to retrieve the list of Java files, count the total number of Java files,
 * and calculate the total number of lines in all Java files.
 */
public class JavaFileScanner {

    // List to store all discovered Java files
    private final List<File> javaFiles;

    // Total number of lines across all Java files
    private int totalLines;

    /**
     * Constructor for JavaFileScanner.
     * Automatically scans the specified directory and processes its contents.
     * @param directoryPath The path of the directory to scan.
     * @throws IllegalArgumentException if the provided path is not a valid directory.
     */
    public JavaFileScanner(String directoryPath) {
        this.javaFiles = new ArrayList<>();
        this.totalLines = 0;

        File directory = new File(directoryPath);
        if (!directory.isDirectory()) {
            Debug.error("Provided path must be a valid directory.");
        }

        // Process the directory upon instantiation
        scanDirectory(directory);
    }

    /**
     * Scans the specified directory and its subdirectories for Java files (*.java).
     * @param directory The root directory to begin scanning.
     */
    private void scanDirectory(File directory) {
        // Iterate through all files and subdirectories in the current directory
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                // Recursively scan the subdirectory
                scanDirectory(file);
            } else if (file.getName().endsWith(".java")) {
                // If the file is a Java file, add it to the list
                javaFiles.add(file);
                // Count lines in the Java file
                countLines(file);
            }
        }
    }

    /**
     * Counts the number of lines in the specified Java file and updates the totalLines counter.
     * @param file The Java file to process.
     */
    private void countLines(File file) {
        try (var linesStream = Files.lines(file.toPath())) {
            int lines = (int) linesStream.count();
            totalLines += lines; // Accumulate the total line count
        } catch (IOException e) {
            // Print a warning message in case of errors reading the file
            System.err.println("Failed to count lines in file: " + file.getName());
        }
    }

    /**
     * Returns the list of discovered Java files.
     * @return A list of File objects representing Java files.
     */
    public List<File> getJavaFiles() {
        return new ArrayList<>(javaFiles); // Return a copy to preserve encapsulation
    }

    /**
     * Returns the total number of Java files discovered.
     * @return The number of Java files.
     */
    public int getNumberOfJavaFiles() {
        return javaFiles.size();
    }

    /**
     * Returns the total number of lines across all Java files.
     * @return The cumulative line count.
     */
    public int getTotalNumberOfLines() {
        return totalLines;
    }
}

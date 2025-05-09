package org.msse696.automation;

import java.io.File;

/**
 * A generic interface for defining different analyzers.
 */
public interface Analyzer {
    /**
     * Analyzes a given class to check for inefficiencies.
     *
     * @param javaFile The File.java file to analyze.
     * @param createReport true will create report in addition to analyze
     * @return True if optimization is needed, false otherwise.
     */
    boolean analyze(File javaFile, boolean createReport);

    /**
     * Generates an HTML report using provided analysis data.
     *
     * @param title          The title of the report.
     * @param actualHeader   Header for the actual analysis data.
     * @param actualData     Data representing inefficiencies found.
     * @param recommendedHeader Header for recommended improvements.
     * @param recommendedData Data representing optimization recommendations.
     * @param outputPath     The file path where the report will be saved.
     */
    void generateReport(String title, String actualHeader, String[][] actualData,
                        String recommendedHeader, String[][] recommendedData, String outputPath);

    /**
     * Returns the path of the generated report file.
     *
     * @return The report file path as a String.
     */
    String getReport();

    /**
     * Returns whether coding practice is efficient or not
     * @return true if coding practice is efficient
     */
    boolean isEfficient();

    /**
     * Returns the recommended optimization examples for inefficient and efficient code.
     *
     * @return A 2D array containing examples of inefficient and efficient code placement.
     */
    String[][] getRecommendedData();
}

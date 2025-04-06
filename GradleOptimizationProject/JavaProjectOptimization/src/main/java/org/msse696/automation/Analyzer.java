package org.msse696.automation;

import java.io.File;

/**
 * A generic interface for defining different analyzers.
 */
public interface Analyzer {

    default boolean analyze(File javaFile) {
        return false;
    }

    void generateReport(String title, String actualHeader, String[][] actualData,
                        String recommendedHeader, String[][] recommendedData, String outputPath);

    String getReport();
}

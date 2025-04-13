package org.msse696.optimization.helper;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JavaFileScannerTest {
    String directoryPath = "C:\\Users\\Rajesh\\Documents\\GitHub\\MSSE696SftwrEngineeringPracticum\\GradleOptimizationProject\\JavaProjectOptimization\\src\\test\\java\\org\\msse696\\optimization";
    /**
     * Test the scanDirectory() method for a valid directory.
     */
    @Test
    void testScanDirectoryValid() throws IOException {
        JavaFileScanner javaFileScanner = new JavaFileScanner(directoryPath);

        int numOfFiles = javaFileScanner.getNumberOfJavaFiles();
        int numOfLines = javaFileScanner.getTotalNumberOfLines();
        List <File> javaFiles = javaFileScanner.getJavaFiles();

        assertEquals(3, numOfFiles);
        assertTrue(numOfLines > 100);
        assertEquals(3, javaFiles.size());
        assertTrue(javaFiles.get(0).getName().contains("java"));
    }
}

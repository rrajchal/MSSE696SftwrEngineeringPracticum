package org.msse696.optimization.helper;

import org.junit.jupiter.api.Test;
import org.msse696.optimization.helper.DataAnalyzer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DataAnalyzerTest {

    @Test
    void testCalculateAverage() {
        DataAnalyzer analyzer = new DataAnalyzer();
        List<Double> data = Arrays.asList(10.0, 20.0, 30.0);
        assertEquals(20.0, analyzer.calculateAverage(data), 0.001);

        List<Double> emptyData = Collections.emptyList();
        assertEquals(0.0, analyzer.calculateAverage(emptyData), 0.001);
    }

    @Test
    void testCalculateStandardDeviation() {
        DataAnalyzer analyzer = new DataAnalyzer();
        List<Double> data = Arrays.asList(10.0, 20.0, 30.0);
        assertEquals(8.165, analyzer.calculateStandardDeviation(data), 0.001);

        List<Double> emptyData = Collections.emptyList();
        assertEquals(0.0, analyzer.calculateStandardDeviation(emptyData), 0.001);
    }

    @Test
    void testIsSignificantlyDifferent() {
        DataAnalyzer analyzer = new DataAnalyzer();
        List<Double> efficientData = Arrays.asList(10.0, 20.0, 30.0);
        List<Double> inefficientData = Arrays.asList(40.0, 50.0, 60.0);

        assertTrue(analyzer.isSignificantlyDifferent(efficientData, inefficientData, 0.05));
        assertTrue(analyzer.isSignificantlyDifferent(efficientData, inefficientData));

        List<Double> identicalData = Arrays.asList(10.0, 20.0, 30.0);
        assertFalse(analyzer.isSignificantlyDifferent(efficientData, identicalData));
    }
}
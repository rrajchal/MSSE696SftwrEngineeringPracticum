package org.msse696.optimization.helper;

interface AnalysisInterface {
    void runEfficientFirstThenInefficient();
    void runInefficientFirstThenEfficient();
    void analyzeData(double efficientPeak, double inefficientPeak);
    void createData(boolean isEfficient, String filePath, int numberOfIterations);
}

package org.msse696.optimization;

import org.msse696.automation.StringConcatenationAnalyzer;

import java.io.File;

public class Main {
    public static void main(String[] args) {
        StringConcatenationAnalyzer analyzer = new StringConcatenationAnalyzer();

        // Analyze StringConcatenationEfficient.java
        File efficientFile = new File("src/main/java/org/msse696/optimization/efficient/StringConcatenationEfficient.java");
        boolean efficientResult = analyzer.analyze(efficientFile, true);


        // Analyze StringConcatenationInefficient.java
        File inefficientFile = new File("src/main/java/org/msse696/optimization/inefficient/StringConcatenationInefficient.java");
        boolean inefficientResult = analyzer.analyze(inefficientFile, true);

    }
}

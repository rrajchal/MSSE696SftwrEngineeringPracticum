// CombinedTestRunner.java
package org.msse696.automation;

import org.junit.jupiter.api.Test;
import org.msse696.automation.testfiles.CombinedTestClass;
import org.msse696.automation.testfiles.CombinedTestClass1;
import org.msse696.optimization.helper.debug.Debug;
import org.msse696.optimization.helper.report.CombinedAnalysisReportGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CombinedTestRunnerTest {
    private final String outputReportPath = "target/results/reports/combined_analysis_report.html";
    @Test
    void testAllClasses() {
        // Enable debug mode if needed
        Debug.setDebugMode(false);

        List<File> filesToAnalyze = new ArrayList<>();
        filesToAnalyze.add(new File("src/test/java/org/msse696/automation/testfiles/CombinedTestClass.java"));
        filesToAnalyze.add(new File("src/test/java/org/msse696/automation/testfiles/CombinedTestClass1.java"));
        // Add paths to other classes you want to analyze

        List<InefficiencyAnalyzerRunner> runners = new ArrayList<>();
        for (File file : filesToAnalyze) {
            InefficiencyAnalyzerRunner runner = new InefficiencyAnalyzerRunner();
            runner.analyze(file, false);
            runners.add(runner);
        }

        CombinedAnalysisReportGenerator.generateReport(runners, outputReportPath, false);
    }
}
package org.msse696.optimization.compare;

import lombok.Getter;
import org.msse696.optimization.efficient.InstanceOfTypeCheckingEfficient;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.inefficient.TryCatchTypeCheckingInefficient;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Hypo7CompareTypeCheckingPerformance {
    private final List<Double> executionTimes = new ArrayList<>();
    final String fileNameCommonExpressionEfficient = "src/data/compare_type_checking_efficient.txt";
    final String fileNameCommonExpressionInefficient = "src/data/compare_type_checking_inefficient.txt";
    FileDataManager fileDataManager;
    public Hypo7CompareTypeCheckingPerformance(boolean isEfficient, int iterations) {
        if (isEfficient) fileDataManager = new FileDataManager(fileNameCommonExpressionEfficient);
        if (!isEfficient) fileDataManager = new FileDataManager(fileNameCommonExpressionInefficient);
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            if (isEfficient) {
                performEfficientTypeChecking(iterations);
            } else {
                performInefficientTypeChecking(iterations);
            }
            long end = System.nanoTime();
            executionTimes.add((double) (end - start)); // Store execution time in nanoseconds
            fileDataManager.appendLine(end - start);
        }
    }

    private void performEfficientTypeChecking(int iterations) {
        InstanceOfTypeCheckingEfficient efficient = new InstanceOfTypeCheckingEfficient();
        efficient.execute(iterations);
    }

    private void performInefficientTypeChecking(int iterations) {
        TryCatchTypeCheckingInefficient inefficient = new TryCatchTypeCheckingInefficient();
        inefficient.execute(iterations);
    }

}

package org.msse696.optimization.compare;

import lombok.Getter;
import org.msse696.optimization.efficient.TryCatchOutsideLoopEfficient;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.inefficient.TryCatchInsideLoopInefficient;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Hypo6CompareTryCatchPerformance {
    private final List<Double> executionTimes = new ArrayList<>();
    final String fileNameCommonExpressionEfficient = "src/data/compare_try_catch_efficient.txt";
    final String fileNameCommonExpressionInefficient = "src/data/compare_try_catch_inefficient.txt";
    FileDataManager fileDataManager;
    public Hypo6CompareTryCatchPerformance(boolean isEfficient, int iterations) {
//        if (isEfficient) fileDataManager = new FileDataManager(fileNameCommonExpressionEfficient);
//        if (!isEfficient) fileDataManager = new FileDataManager(fileNameCommonExpressionInefficient);
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            if (isEfficient) {
                performEfficientCalculation(iterations);
            } else {
                performInefficientCalculation(iterations);
            }
            long end = System.nanoTime();
            executionTimes.add((double) (end - start)); // Store execution time in nanoseconds
            //fileDataManager.appendLine(end - start);
        }
    }

    private void performEfficientCalculation(int iterations) {
        TryCatchOutsideLoopEfficient efficient = new TryCatchOutsideLoopEfficient();
        efficient.execute(iterations);
    }

    private void performInefficientCalculation(int iterations) {
        TryCatchInsideLoopInefficient inefficient = new TryCatchInsideLoopInefficient();
        inefficient.execute(iterations);
    }

}

package org.msse696.optimization.compare;

import lombok.Getter;
import org.msse696.optimization.efficient.ExpressionEliminationEfficient;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.inefficient.ExpressionEliminationInefficient;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Hypo5CompareCommonExpressions {
    private final List<Double> executionTimes = new ArrayList<>();
    final String fileNameCommonExpressionEfficient = "src/data/compare_common_expression_efficient.txt";
    final String fileNameCommonExpressionInefficient = "src/data/compare_common_expression_inefficient.txt";
    FileDataManager fileDataManager;
    public Hypo5CompareCommonExpressions(boolean isEfficient, int iterations) {
        if (isEfficient) fileDataManager = new FileDataManager(fileNameCommonExpressionEfficient);
        if (!isEfficient) fileDataManager = new FileDataManager(fileNameCommonExpressionInefficient);
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            if (isEfficient) {
                performEfficientCalculation(iterations);
            } else {
                performInefficientCalculation(iterations);
            }
            long end = System.nanoTime();
            executionTimes.add((double) (end - start)); // Store execution time in nanoseconds
            //fileDataManager.appendLine(end-start);
        }
    }

    private void performEfficientCalculation(int iterations) {
        ExpressionEliminationEfficient efficient = new ExpressionEliminationEfficient();
        efficient.calculate(iterations);
    }

    private void performInefficientCalculation(int iterations) {
        ExpressionEliminationInefficient inefficient = new ExpressionEliminationInefficient();
        inefficient.calculate(iterations);
    }

}

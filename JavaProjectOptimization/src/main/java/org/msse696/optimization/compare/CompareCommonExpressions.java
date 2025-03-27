package org.msse696.optimization.compare;

import lombok.Getter;
import org.msse696.optimization.efficient.ExpressionEliminationEfficient;
import org.msse696.optimization.inefficient.ExpressionEliminationInefficient;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CompareCommonExpressions {
    private final List<Double> executionTimes = new ArrayList<>();

    public CompareCommonExpressions(boolean isEfficient, int iterations) {
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            if (isEfficient) {
                performEfficientCalculation(iterations);
            } else {
                performInefficientCalculation(iterations);
            }
            long end = System.nanoTime();
            executionTimes.add((double) (end - start)); // Store execution time in nanoseconds
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

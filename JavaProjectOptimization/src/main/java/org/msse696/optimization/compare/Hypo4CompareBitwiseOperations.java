package org.msse696.optimization.compare;

import lombok.Getter;
import org.msse696.optimization.efficient.BitwiseOperationEfficient;
import org.msse696.optimization.inefficient.ArithmeticOperationInefficient;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Hypo4CompareBitwiseOperations {
    private final List<Double> executionTimes = new ArrayList<>();
    public Hypo4CompareBitwiseOperations(boolean isEfficient, int iterations) {
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
        BitwiseOperationEfficient efficient = new BitwiseOperationEfficient();
        efficient.performOperations(iterations);
    }

    private void performInefficientCalculation(int iterations) {
        ArithmeticOperationInefficient inefficient = new ArithmeticOperationInefficient();
        inefficient.performOperations(iterations);
    }

}

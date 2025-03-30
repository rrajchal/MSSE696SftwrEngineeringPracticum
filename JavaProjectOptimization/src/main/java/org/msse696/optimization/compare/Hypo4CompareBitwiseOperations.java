package org.msse696.optimization.compare;

import lombok.Getter;
import org.msse696.optimization.efficient.BitwiseOperationEfficient;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.inefficient.ArithmeticOperationInefficient;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Hypo4CompareBitwiseOperations {
    private final List<Double> executionTimes = new ArrayList<>();
    final String fileNameCommonExpressionEfficient = "src/data/compare_bitwise_efficient4.txt";
    final String fileNameCommonExpressionInefficient = "src/data/compare_bitwise_inefficient4.txt";
    FileDataManager fileDataManager;
    public Hypo4CompareBitwiseOperations(boolean isEfficient, int iterations) {
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
            //fileDataManager.appendLine(end - start);
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

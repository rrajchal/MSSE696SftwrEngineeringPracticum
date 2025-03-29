package org.msse696.optimization.compare;

import lombok.Getter;
import org.msse696.optimization.efficient.CopyArrayEfficient;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.inefficient.CopyArrayInefficient;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Hypo10CompareArrayCopyPerformance {
    private final List<Double> executionTimes = new ArrayList<>();
    final String fileNameCommonExpressionEfficient = "src/data/compare_array_copy_efficient10.txt";
    final String fileNameCommonExpressionInefficient = "src/data/compare_array_copy_inefficient10.txt";
    FileDataManager fileDataManager;
    public Hypo10CompareArrayCopyPerformance(boolean isEfficient, int iterations, int arraySize) {
        if (isEfficient) fileDataManager = new FileDataManager(fileNameCommonExpressionEfficient);
        if (!isEfficient) fileDataManager = new FileDataManager(fileNameCommonExpressionInefficient);
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            if (isEfficient) {
                performEfficientCopy(arraySize);
            } else {
                performInefficientCopy(arraySize);
            }
            long end = System.nanoTime();
            executionTimes.add((double) (end - start)); // Store execution time in nanoseconds
            //fileDataManager.appendLine(end - start);
        }
    }

    private void performEfficientCopy(int arraySize) {
        CopyArrayEfficient efficient = new CopyArrayEfficient();
        efficient.copyArray(arraySize);
    }

    private void performInefficientCopy(int arraySize) {
        CopyArrayInefficient inefficient = new CopyArrayInefficient();
        inefficient.copyArray(arraySize);
    }

}

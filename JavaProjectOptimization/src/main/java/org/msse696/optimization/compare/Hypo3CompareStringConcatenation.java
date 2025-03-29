package org.msse696.optimization.compare;

import lombok.Getter;
import org.msse696.optimization.efficient.StringConcatenationEfficient;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.inefficient.StringConcatenationInefficient;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Hypo3CompareStringConcatenation {
    private final List<Double> executionTimes = new ArrayList<>();
    final String fileNameCommonExpressionEfficient = "src/data/compare_concatenate_string_efficient3.txt";
    final String fileNameCommonExpressionInefficient = "src/data/compare_concatenate_string_inefficient3.txt";
    FileDataManager fileDataManager;
    public Hypo3CompareStringConcatenation(boolean isEfficient, int iterations) {
        if (isEfficient) fileDataManager = new FileDataManager(fileNameCommonExpressionEfficient);
        if (!isEfficient) fileDataManager = new FileDataManager(fileNameCommonExpressionInefficient);

        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            if (isEfficient) {
                performEfficientConcatenation(iterations);
            } else {
                performInefficientConcatenation(iterations);
            }
            long end = System.nanoTime();
            //fileDataManager.appendLine((end - start));
            executionTimes.add((double) (end - start));
        }
    }

    private void performEfficientConcatenation(int iterations) {
        StringConcatenationEfficient efficient = new StringConcatenationEfficient();
        efficient.concatenateStrings(iterations);
    }

    private void performInefficientConcatenation(int iterations) {
        StringConcatenationInefficient inefficient = new StringConcatenationInefficient();
        inefficient.concatenateStrings(iterations);
    }

}

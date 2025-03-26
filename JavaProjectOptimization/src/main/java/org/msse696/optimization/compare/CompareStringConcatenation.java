package org.msse696.optimization.compare;

import org.msse696.optimization.efficient.StringConcatenationEfficient;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.inefficient.StringConcatenationInefficient;

public class CompareStringConcatenation {
    public CompareStringConcatenation(boolean isEfficient, String fileName, int iterations) {
        FileDataManager fileDataManager = new FileDataManager(fileName);
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            if (isEfficient) {
                performEfficientConcatenation(iterations);
            } else {
                performInefficientConcatenation(iterations);
            }
            long end = System.nanoTime();
            fileDataManager.appendLine((end - start));
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

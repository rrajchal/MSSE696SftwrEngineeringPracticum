package org.msse696.optimization.compare;

import lombok.Getter;
import org.msse696.optimization.efficient.ObjectCreationEfficient;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.inefficient.ObjectCreationInefficient;

import java.util.ArrayList;
import java.util.List;

public class Hypo2CompareObjectCreation {
    @Getter
    private final List<Double> executionTimes = new ArrayList<>();
    final String fileNameCommonExpressionEfficient = "src/data/compare_object_creation_efficient2.txt";
    final String fileNameCommonExpressionInefficient = "src/data/compare_object_creation_inefficient2.txt";
    FileDataManager fileDataManager;
    public Hypo2CompareObjectCreation(boolean isEfficient, int iterations) {
        if (isEfficient) fileDataManager = new FileDataManager(fileNameCommonExpressionEfficient);
        if (!isEfficient) fileDataManager = new FileDataManager(fileNameCommonExpressionInefficient);
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            if (isEfficient) {
                compareEfficientObjectCreation(iterations);
            } else {
                compareInefficientObjectCreation(iterations);
            }
            long end = System.nanoTime();
            //executionTimes.add((double) (end - start)); // Store execution time in nanoseconds
            fileDataManager.appendLine(end - start);
        }
    }
    public void compareEfficientObjectCreation(int numberOfTimes) {
        //ComputationUtils.performNComputation();
        ObjectCreationEfficient efficient = new ObjectCreationEfficient();
        efficient.processUsers(numberOfTimes);
    }

    public void compareInefficientObjectCreation(int numberOfTimes) {
        //ComputationUtils.performNComputation();
        ObjectCreationInefficient inefficient = new ObjectCreationInefficient();
        inefficient.processUsers(numberOfTimes);
    }

}

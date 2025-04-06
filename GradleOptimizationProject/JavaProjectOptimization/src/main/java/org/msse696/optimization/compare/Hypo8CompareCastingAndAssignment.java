package org.msse696.optimization.compare;

import lombok.Getter;
import org.msse696.optimization.efficient.CastingAssignmentEfficient;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.inefficient.CastingAssignmentInefficient;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Hypo8CompareCastingAndAssignment {
    private final List<Double> executionTimes = new ArrayList<>();
    final String fileNameCommonExpressionEfficient = "src/data/compare_casting_assignment_efficient8.txt";
    final String fileNameCommonExpressionInefficient = "src/data/compare_casting_assignment_inefficient8.txt";
    FileDataManager fileDataManager;
    public Hypo8CompareCastingAndAssignment(boolean isEfficient, int iterations) {
        if (isEfficient) fileDataManager = new FileDataManager(fileNameCommonExpressionEfficient);
        if (!isEfficient) fileDataManager = new FileDataManager(fileNameCommonExpressionInefficient);
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            if (isEfficient) {
                performEfficientAssignment(iterations);
            } else {
                performInefficientCasting(iterations);
            }
            long end = System.nanoTime();
            executionTimes.add((double) (end - start)); // Store execution time in nanoseconds
            //fileDataManager.appendLine(end - start);
        }
    }

    private void performEfficientAssignment(int iterations) {
        CastingAssignmentEfficient efficient = new CastingAssignmentEfficient();
        efficient.execute(iterations);
    }

    private void performInefficientCasting(int iterations) {
        CastingAssignmentInefficient inefficient = new CastingAssignmentInefficient();
        inefficient.execute(iterations);
    }

}

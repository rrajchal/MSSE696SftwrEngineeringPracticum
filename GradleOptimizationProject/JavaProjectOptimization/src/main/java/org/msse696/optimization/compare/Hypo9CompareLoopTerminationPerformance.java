package org.msse696.optimization.compare;

import lombok.Getter;
import org.msse696.optimization.efficient.LoopTerminationnefficient;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.inefficient.LoopTerminationInefficient;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Hypo9CompareLoopTerminationPerformance {
    private final List<Double> executionTimes = new ArrayList<>();
    final String fileNameCommonExpressionEfficient = "src/data/compare_loop_termination_efficient9.txt";
    final String fileNameCommonExpressionInefficient = "src/data/compare_loop_termination_inefficient9.txt";
    FileDataManager fileDataManager;
    public Hypo9CompareLoopTerminationPerformance(boolean isEfficient, int iterations) {
        if (isEfficient) fileDataManager = new FileDataManager(fileNameCommonExpressionEfficient);
        if (!isEfficient) fileDataManager = new FileDataManager(fileNameCommonExpressionInefficient);
        for (int i = 0; i < iterations; i++) {
            long start = System.nanoTime();
            if (isEfficient) {
                performEfficientLoopTermination();
            } else {
                performInefficientLoopTermination();
            }
            long end = System.nanoTime();
            executionTimes.add((double) (end - start)); // Store execution time in nanoseconds
            //fileDataManager.appendLine(end - start);
        }
    }

    private void performEfficientLoopTermination() {
        LoopTerminationnefficient efficient = new LoopTerminationnefficient();
        efficient.execute();
    }

    private void performInefficientLoopTermination() {
        LoopTerminationInefficient inefficient = new LoopTerminationInefficient();
        inefficient.execute();
    }

}

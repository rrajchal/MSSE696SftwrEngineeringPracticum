package org.msse696.optimization.compare;

import lombok.Getter;
import org.msse696.optimization.efficient.ObjectCreationEfficient;
import org.msse696.optimization.inefficient.ObjectCreationInefficient;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Hypo1CompareVariableDeclarationOrderPerformance {
    private final List<Double> executionTimes = new ArrayList<>();

    public Hypo1CompareVariableDeclarationOrderPerformance(boolean isOptimized, int count) {
        for (int i = 0; i < count; i++) {
            long start = System.nanoTime();
            if (isOptimized) {
                new ObjectCreationEfficient(); // Create optimized objects
            } else {
                new ObjectCreationInefficient(); // Create unoptimized objects
            }
            long end = System.nanoTime();
            executionTimes.add((double) (end - start)); // Store object creation time
        }
    }

}

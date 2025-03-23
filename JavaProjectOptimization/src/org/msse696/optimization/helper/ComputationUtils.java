package org.msse696.optimization.helper;

public class ComputationUtils {

    public static void performNComputation() {
        performNComputation(1000); // Default is 1000
    }

    public static void performNComputation(int iterations) {
        for (int i = 0; i < iterations; i++) {
            Math.sqrt(i);
        }
    }
}

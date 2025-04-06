package org.msse696.optimization.helper;

public class ComputationUtils {

    public static void performNComputation() {
       performNComputation(1000); // Default is 1000
    }

    public static void performNComputation(int iterations) {
        double result = 0;
        for (int i = 0; i < iterations; i++) {
            for (int j = 0; j < 10; j++) {
                result += Math.sqrt(i * j);
            }
        }
    }
}

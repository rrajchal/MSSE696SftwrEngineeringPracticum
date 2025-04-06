package org.msse696.optimization.inefficient;

public class ArithmeticOperationInefficient {
    public void performOperations(int iterations) {
        int result = 1;
        for (int i = 0; i < iterations; i++) {
            result = result * 2; // Multiply by 2
            result = result / 2; // Divide by 2
        }
    }
}

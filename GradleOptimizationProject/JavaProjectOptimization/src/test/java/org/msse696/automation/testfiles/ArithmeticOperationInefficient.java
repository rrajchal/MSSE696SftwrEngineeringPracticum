package org.msse696.automation.testfiles;

public class ArithmeticOperationInefficient {
    public void performOperations(int iterations) {
        int result = 1;
        for (int i = 0; i < iterations; i++) {
            result = result * 2; // Inefficient multiplication by 2
            result = result / 2; // Inefficient division by 2
        }
    }
}

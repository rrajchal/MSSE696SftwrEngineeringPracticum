package org.msse696.optimization.efficient;

public class BitwiseOperationEfficient {
    public void performOperations(int iterations) {
        int result = 1;
        for (int i = 0; i < iterations; i++) {
            result = result << 1; // Bitwise left shift (multiply by 2)
            result = result >> 1; // Bitwise right shift (divide by 2)
        }
    }

    public void performOperationsAlternatives(int iterations) {
        int result = 1;
        for (int i = 0; i < iterations; i++) {
            result *= 2; // Equivalent to multiplying by 2
            result /= 2; // Equivalent to dividing by 2
        }
    }
}

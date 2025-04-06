package org.msse696.automation.testfiles;

public class BitwiseOperationEfficient {
    public void performOperations(int iterations) {
        int result = 1;
        for (int i = 0; i < iterations; i++) {
            result = result << 1; // Efficient: Bitwise left shift (multiply by 2)
            result = result >> 1; // Efficient: Bitwise right shift (divide by 2)
        }
    }
}

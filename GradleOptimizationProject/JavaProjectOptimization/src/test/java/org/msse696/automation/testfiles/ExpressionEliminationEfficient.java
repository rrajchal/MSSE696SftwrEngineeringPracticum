package org.msse696.automation.testfiles;

public class ExpressionEliminationEfficient {
    public void calculate(int iterations) {
        for (int i = 0; i < iterations; i++) {
            int x = 10;
            int y = 5;
            // Temporary variable to store the common subexpression
            int t1 = x * Math.abs(y);
            int z1 = t1 + x;
            int z2 = t1 + y;
            int z = z1 + z2;
        }
    }
}

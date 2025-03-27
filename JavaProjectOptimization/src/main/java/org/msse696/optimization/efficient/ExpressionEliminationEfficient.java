package org.msse696.optimization.efficient;

public class ExpressionEliminationEfficient {
    public void calculate(int iterations) {
        for (int i = 0; i < iterations; i++) {
            int x = 10;
            int y = 5;
            int t1 = x * Math.abs(y); // Temporary variable to store the common subexpression
            int z1 = t1 + x;
            int z2 = t1 + y;
            int z = z1 + z2;
        }
    }
}

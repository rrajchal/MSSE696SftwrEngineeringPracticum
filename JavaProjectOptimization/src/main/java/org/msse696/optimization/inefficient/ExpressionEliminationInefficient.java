package org.msse696.optimization.inefficient;

public class ExpressionEliminationInefficient {
    public void calculate(int iterations) {
        for (int i = 0; i < iterations; i++) {
            int x = 10;
            int y = 5;
            int z1 = x * Math.abs(y) + x; // Common subexpression calculated twice
            int z2 = x * Math.abs(y) + y; // Common subexpression calculated again
            int z = z1 + z2;
        }
    }
}

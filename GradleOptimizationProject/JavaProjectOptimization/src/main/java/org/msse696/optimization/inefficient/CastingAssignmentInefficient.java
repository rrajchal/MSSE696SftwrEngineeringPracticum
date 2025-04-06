package org.msse696.optimization.inefficient;

public class CastingAssignmentInefficient {
    public void execute(int iterations) {
        for (int i = 0; i < iterations; i++) {
            Integer castedValue = (Integer) i; // Explicit casting
            int total = castedValue + castedValue;
        }
    }
}


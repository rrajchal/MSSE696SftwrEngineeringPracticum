package org.msse696.optimization.efficient;

public class CastingAssignmentEfficient {
    public void execute(int iterations) {
        for (int i = 0; i < iterations; i++) {
            Integer assignedValue = i; // Direct assignment
            int total = assignedValue + assignedValue; // using castedValue
        }
    }
}

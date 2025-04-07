package org.msse696.automation.testfiles;

public class CastingInefficient {
    public void execute(int iterations) {
        for (int i = 0; i < iterations; i++) {
            Integer assignedValue = (Integer) i; // Explicit casting
            int total = assignedValue + assignedValue;
        }
    }
}

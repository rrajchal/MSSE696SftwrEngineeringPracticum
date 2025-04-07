package org.msse696.automation.testfiles;

public class CastingEfficient {
    public void execute(int iterations) {
        for (int i = 0; i < iterations; i++) {
            Integer assignedValue = i; // Direct assignment
            int total = assignedValue + assignedValue;
        }
    }
}

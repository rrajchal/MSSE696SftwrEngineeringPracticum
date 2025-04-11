package org.msse696.automation.testfiles;

import org.msse696.optimization.helper.debug.Debug;

public class InstanceofInefficient {
    public void execute(int iterations) {
        Object obj = "123"; // Example object as a String representing a number
        for (int i = 0; i < iterations; i++) {
            try {
                performOperation(obj);
            } catch (ClassCastException | NumberFormatException e) {
                Debug.error("Invalid cast or format: " + e.getMessage());
            }
        }
    }

    private void performOperation(Object obj) {
        // Mock operation logic
        Debug.info("Performing operation on: " + obj);
    }
}

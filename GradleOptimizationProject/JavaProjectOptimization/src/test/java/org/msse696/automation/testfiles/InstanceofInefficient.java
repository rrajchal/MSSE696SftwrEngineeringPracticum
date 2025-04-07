package org.msse696.automation.testfiles;

public class InstanceofInefficient {
    public void execute(int iterations) {
        Object obj = "123"; // Example object as a String representing a number
        for (int i = 0; i < iterations; i++) {
            try {
                performOperation(obj);
            } catch (ClassCastException | NumberFormatException e) {
                System.err.println("Invalid cast or format: " + e.getMessage());
            }
        }
    }

    private void performOperation(Object obj) {
        // Mock operation logic
        System.out.println("Performing operation on: " + obj);
    }
}

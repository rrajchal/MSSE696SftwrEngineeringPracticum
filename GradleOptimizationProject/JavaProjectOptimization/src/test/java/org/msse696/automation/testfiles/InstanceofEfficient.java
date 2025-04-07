package org.msse696.automation.testfiles;

public class InstanceofEfficient {
    public void execute(int iterations) {
        Object obj = "123"; // Example object as a String representing a number
        for (int i = 0; i < iterations; i++) {
            if (obj instanceof String) {
                performOperation(obj);
            }
        }
    }

    private void performOperation(Object obj) {
        // Mock operation logic
        System.out.println("Performing operation on: " + obj);
    }
}

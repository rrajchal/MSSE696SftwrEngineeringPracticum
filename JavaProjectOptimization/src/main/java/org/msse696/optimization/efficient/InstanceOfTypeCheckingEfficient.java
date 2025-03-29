package org.msse696.optimization.efficient;

public class InstanceOfTypeCheckingEfficient {
    public void execute(int iterations) {
        Object obj = "123"; // Example object as a String representing a number
        for (int i = 0; i < iterations; i++) {
            if (obj instanceof String) {
                performOperation(obj);
            }
        }
    }

    private void performOperation(Object str) {
        // Simulated operation: safely cast to Integer
        Integer value = Integer.parseInt(str.toString());
        value.toString(); // Perform a trivial operation to simulate usage
    }
}

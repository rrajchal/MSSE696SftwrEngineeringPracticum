package org.msse696.optimization.inefficient;

public class TryCatchTypeCheckingInefficient {
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

    private void performOperation(Object obj) throws NumberFormatException {
        // Attempt to cast the object to an Integer directly
        Integer value = Integer.parseInt(obj.toString());
        value.toString(); // Perform a trivial operation to simulate usage
    }
}

package org.msse696.optimization.inefficient;

public class TryCatchInsideLoopInefficient {
    public void execute(int iterations) {
        for (int i = 0; i < iterations; i++) {
            try {
                //performOperation(i);
                performOperation();
            } catch (Exception e) {
                System.err.println("An exception occurred: " + e.getMessage());
            }
        }
    }

    private void performOperation() throws Exception {
        String result = "";
        for (int i = 0; i < 10; i++) {
            result += i;
        }
        result = result + "1";
    }

    private void performOperation(int value) throws Exception {
        if (value > 10 && value % 10 == 0) { // Simulate occasional exception
            throw new Exception("Simulated exception for value: " + value);
        }
    }
}

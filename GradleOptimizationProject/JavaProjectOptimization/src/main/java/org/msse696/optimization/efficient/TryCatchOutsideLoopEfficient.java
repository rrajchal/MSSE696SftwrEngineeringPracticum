package org.msse696.optimization.efficient;

public class TryCatchOutsideLoopEfficient {
    public void execute(int iterations) {
        try {
            for (int i = 0; i < iterations; i++) {
                performOperation();
            }
        } catch (Exception e) {
            System.err.println("An exception occurred: " + e.getMessage());
        }
    }

    private String performOperation() {
        String result = "";
        for (int i = 0; i < 10; i++) {
            result += i;
        }
        result = result + "1";
        return result;
    }
}

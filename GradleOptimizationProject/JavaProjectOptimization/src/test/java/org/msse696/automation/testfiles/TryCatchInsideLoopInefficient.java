package org.msse696.automation.testfiles;

public class TryCatchInsideLoopInefficient {
    public void execute(int iterations) {
        for (int i = 0; i < iterations; i++) {
            try {
                performOperation();
            } catch (Exception e) {
                System.err.println("An exception occurred: " + e.getMessage());
            }
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

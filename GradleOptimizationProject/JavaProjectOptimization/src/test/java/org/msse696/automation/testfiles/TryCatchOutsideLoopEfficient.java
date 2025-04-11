package org.msse696.automation.testfiles;

import org.msse696.optimization.helper.debug.Debug;

public class TryCatchOutsideLoopEfficient {
    public void execute(int iterations) {
        try {
            for (int i = 0; i < iterations; i++) {
                performOperation();
            }
        } catch (Exception e) {
            Debug.error("An exception occurred: " + e.getMessage());
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
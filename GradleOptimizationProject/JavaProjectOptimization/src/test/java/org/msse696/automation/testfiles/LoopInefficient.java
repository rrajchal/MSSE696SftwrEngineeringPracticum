package org.msse696.automation.testfiles;

import org.msse696.optimization.helper.debug.Debug;

public class LoopInefficient {
    public void execute(String size) {
        for (int i = 0; i < size.length(); i++) { // Method called during iteration
            performOperation(size.charAt(i));
        }
    }

    private void performOperation(char c) {
        // Mock operation logic
        Debug.info("Performing operation on: " + c);
    }
}

package org.msse696.automation.testfiles;

import org.msse696.optimization.helper.debug.Debug;

public class LoopEfficient {
    public void execute(String size) {
        int length = size.length(); // Precompute loop limit
        for (int i = 0; i < length; i++) {
            performOperation(size.charAt(i));
        }
    }

    private void performOperation(char c) {
        // Mock operation logic
        Debug.info("Performing operation on: " + c);
    }
}

package org.msse696.automation.testfiles;

public class LoopEfficient {
    public void execute(String size) {
        int length = size.length(); // Precompute loop limit
        for (int i = 0; i < length; i++) {
            performOperation(size.charAt(i));
        }
    }

    private void performOperation(char c) {
        // Mock operation logic
        System.out.println("Performing operation on: " + c);
    }
}

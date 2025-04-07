package org.msse696.automation.testfiles;

public class LoopInefficient {
    public void execute(String size) {
        for (int i = 0; i < size.length(); i++) { // Method called during iteration
            performOperation(size.charAt(i));
        }
    }

    private void performOperation(char c) {
        // Mock operation logic
        System.out.println("Performing operation on: " + c);
    }
}

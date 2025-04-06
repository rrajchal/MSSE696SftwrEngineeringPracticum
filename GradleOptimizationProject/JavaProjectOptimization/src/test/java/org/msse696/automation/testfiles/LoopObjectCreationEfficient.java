package org.msse696.automation.testfiles;

public class LoopObjectCreationEfficient {
    public void testMethod() {
        String obj = new String("Test");  // Object created outside the loop
        for (int i = 0; i < 10; i++) {
            // Reuse the object
        }
    }
}

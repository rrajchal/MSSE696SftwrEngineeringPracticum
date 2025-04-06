package org.msse696.automation.testfiles;

public class LoopObjectCreationInefficient {
    public void testMethod() {
        for (int i = 0; i < 10; i++) {
            String obj = new String("Test");  // Inefficient object creation inside loop
        }
    }
}

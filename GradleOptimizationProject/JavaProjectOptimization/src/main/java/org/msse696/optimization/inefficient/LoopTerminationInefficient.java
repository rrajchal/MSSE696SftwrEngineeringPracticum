package org.msse696.optimization.inefficient;

import org.msse696.optimization.helper.GenerateRandomObjects;

import java.util.Random;

public class LoopTerminationInefficient {
    public void execute() {
        String size = GenerateRandomObjects.generateRandomString(10000);
        for (int i = 0; i < size.length(); i++) { // Method called during iteration
            performOperation(size.charAt(i));
        }
    }

    private void performOperation(char character) {
        character = Character.toUpperCase(character);
    }
}

package org.msse696.optimization.inefficient;

import org.msse696.optimization.helper.GenerateRandomObjects;

import java.util.Random;

public class LoopTerminationInefficient {
    public void execute() {
        String size = GenerateRandomObjects.generateRandomString(10000);
        // Method called during iteration
        for (int i = 0; i < size.length(); i++) {
            performOperation(i);
        }
    }

    private void performOperation(int character) {
        character = Character.toUpperCase(character);
    }
}

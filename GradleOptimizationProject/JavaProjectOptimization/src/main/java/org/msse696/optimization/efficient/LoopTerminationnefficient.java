package org.msse696.optimization.efficient;

import org.msse696.optimization.helper.GenerateRandomObjects;

import java.util.Random;

public class LoopTerminationnefficient {
    public void execute() {
        String size = GenerateRandomObjects.generateRandomString(10000);
        int length = size.length(); // Precompute loop limit
        for (int i = 0; i < length; i++) {
            performOperation(size.charAt(i));
        }
    }

    private void performOperation(char character) {
        character = Character.toUpperCase(character);
    }
}

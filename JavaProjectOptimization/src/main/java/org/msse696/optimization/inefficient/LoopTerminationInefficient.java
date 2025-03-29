package org.msse696.optimization.inefficient;

import java.util.Random;

public class LoopTerminationInefficient {
    public void execute() {
        String size = generateRandomString(10000); // Generate a string with 10,000 characters
        for (int i = 0; i < size.length(); i++) { // Method called during every iteration
            performOperation(size.charAt(i));
        }
    }

    private void performOperation(char character) {
        // Simulated operation
        character = Character.toUpperCase(character);
    }

    private String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char randomChar = (char) ('a' + random.nextInt(26)); // Generate a random lowercase letter
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }
}

package org.msse696.optimization.helper;

import java.util.Random;

public class GenerateRandomObjects {
    // Generate a string with 10,000 characters
    public static String generateRandomString(int length) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            // Generate a random lowercase letter
            char randomChar = (char) ('a' + random.nextInt(26));
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }
}

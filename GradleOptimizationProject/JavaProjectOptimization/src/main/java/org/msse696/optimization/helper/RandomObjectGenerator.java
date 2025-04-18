package org.msse696.optimization.helper;

import java.util.Random;

public class RandomObjectGenerator {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final Random RANDOM = new Random();
    private static final int INTEGER_PROBABILITY = 50; // Percentage chance of generating an Integer
    private static final int STRING_LENGTH = 5;

    public static Object generateRandomObject() {
        if (RANDOM.nextInt(100) < INTEGER_PROBABILITY) {
            return RANDOM.nextInt(1000);
        } else {
            return generateRandomString();
        }
    }

    private static String generateRandomString() {
        StringBuilder sb = new StringBuilder(STRING_LENGTH);
        for (int i = 0; i < STRING_LENGTH; i++) {
            sb.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}
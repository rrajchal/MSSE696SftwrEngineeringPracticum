package org.msse696.optimization.efficient;

public class StringConcatenationEfficient {
    public String concatenateStrings(int iterations) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            result.append(i);
        }
        return result.toString();
    }
}

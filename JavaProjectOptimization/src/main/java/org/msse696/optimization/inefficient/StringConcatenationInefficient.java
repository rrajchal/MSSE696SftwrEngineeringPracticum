package org.msse696.optimization.inefficient;

public class StringConcatenationInefficient {
    public String concatenateStrings(int iterations) {
        String result = "";
        for (int i = 0; i < iterations; i++) {
            result += i;
        }
        return result;
    }
}

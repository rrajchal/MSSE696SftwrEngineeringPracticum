package org.msse696.optimization.inefficient;

import java.util.Arrays;

public class CopyArrayInefficient {
    public void copyArray(int size) {
        int[] source = new int[size];
        int[] destination = new int[size];

        Arrays.fill(source, 1); // Fill source array with dummy data
        for (int i = 0; i < size; i++) { // Manual copy using a loop
            destination[i] = source[i];
        }
        int arrSize = destination.length;
    }
}

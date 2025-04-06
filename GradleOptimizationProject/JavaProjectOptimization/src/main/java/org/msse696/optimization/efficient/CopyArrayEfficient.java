package org.msse696.optimization.efficient;

import java.util.Arrays;

public class CopyArrayEfficient {
    public void copyArray(int size) {
        int[] source = new int[size];
        int[] destination = new int[size];
        // Fill source array with dummy data
        Arrays.fill(source, 1);

        // Use System.arraycopy
        System.arraycopy(source, 0, destination, 0, size);
    }

    public void copyArray1(int size) {
        int[] source = new int[size];

        Arrays.fill(source, 1); // Fill source array with dummy data
        int[] destination = source.clone();
        int arrSize = destination.length;
    }
}

package org.msse696.optimization.efficient;

import java.util.Arrays;

public class CopyArrayEfficient {
    public void copyArray1(int size) {
        int[] source = new int[size];
        int[] destination = new int[size];

        Arrays.fill(source, 1); // Fill source array with dummy data
        System.arraycopy(source, 0, destination, 0, size); // Use System.arraycopy
    }

    public void copyArray(int size) {
        int[] source = new int[size];

        Arrays.fill(source, 1); // Fill source array with dummy data
        int[] destination = source.clone();
        int arrSize = destination.length;
    }
}

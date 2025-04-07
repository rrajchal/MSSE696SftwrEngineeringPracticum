package org.msse696.automation.testfiles;

import java.util.Arrays;

public class ArrayCopyInefficient {
    public void copyArray(int size) {
        int[] source = new int[size];
        int[] destination = new int[size];
        // Fill source array with dummy data
        Arrays.fill(source, 1);

        // Manual copy using a loop
        for (int i = 0; i < size; i++) {
            destination[i] = source[i];
        }
    }
}

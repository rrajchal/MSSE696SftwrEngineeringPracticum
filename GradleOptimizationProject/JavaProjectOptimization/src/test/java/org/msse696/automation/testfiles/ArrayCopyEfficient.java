package org.msse696.automation.testfiles;

import java.util.Arrays;

public class ArrayCopyEfficient {
    public void copyArray(int size) {
        int[] source = new int[size];
        int[] destination = new int[size];
        // Fill source array with dummy data
        Arrays.fill(source, 1);

        // Use System.arraycopy
        System.arraycopy(source, 0, destination, 0, size);
    }
}

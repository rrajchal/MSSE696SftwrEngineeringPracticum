package org.msse696.optimization.helper;

public class HeapSizeChecker {
    public static void main(String[] args) {
        // Get the Java Runtime instance
        Runtime runtime = Runtime.getRuntime();

        // Print heap memory details in bytes
        System.out.println("Max Heap Size: " + runtime.maxMemory() / (1024 * 1024) + " MB");
        System.out.println("Initial Heap Size (Free): " + runtime.freeMemory() / (1024 * 1024) + " MB");
        System.out.println("Total Heap Size (Allocated): " + runtime.totalMemory() / (1024 * 1024) + " MB");
    }
}

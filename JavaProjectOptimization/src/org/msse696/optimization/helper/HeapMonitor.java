package org.msse696.optimization.helper;

public class HeapMonitor {
    private static volatile boolean running = true;
    private static long maxHeapUsed = 0;

    public static void startMonitoring() {
        Thread monitorThread = new Thread(() -> {
            Runtime runtime = Runtime.getRuntime();
            while (running) {
                long totalHeap = runtime.totalMemory(); // Allocated heap size
                long freeHeap = runtime.freeMemory(); // Free memory in the heap
                long usedHeap = totalHeap - freeHeap; // Used memory in the heap
                // Update maximum heap used
                maxHeapUsed = Math.max(maxHeapUsed, usedHeap);
                try {
                    Thread.sleep(100); // Check heap usage every millisecond
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        monitorThread.setDaemon(true); // Ensure the thread terminates with the program
        monitorThread.start();
    }

    public static void stopMonitoring() {
        running = false; // Stop the monitoring thread
    }

    public static void printMaxHeapUsed() {
        System.out.println("Maximum Heap Used During Execution: " + (maxHeapUsed / (1024 * 1024)) + " MB");
    }
}

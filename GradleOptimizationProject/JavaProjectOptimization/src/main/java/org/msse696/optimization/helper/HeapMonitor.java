package org.msse696.optimization.helper;

public class HeapMonitor {
    private static long peakHeapUsage = 0;
    // Ensures that the running flag's value is always read directly from main memory and not from a thread's local cache.
    private static volatile boolean running = true;
    private static Thread monitorThread = null; // Keep track of the monitoring thread

    public static void startMonitoring() {
        if (monitorThread != null && monitorThread.isAlive()) {
            return; // If a monitoring thread is already running, do nothing
        }
        running = true; // Set the running flag to true
        monitorThread = new Thread(() -> {
            Runtime runtime = Runtime.getRuntime();
            while (running) {
                long usedHeap = runtime.totalMemory() - runtime.freeMemory();
                peakHeapUsage = Math.max(peakHeapUsage, usedHeap); // Update peak memory usage

                try {
                    Thread.sleep(100); // Monitor every 100ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });
        monitorThread.setDaemon(true); // Ensure the thread doesn't block JVM shutdown
        monitorThread.start();
    }

    public static void stopMonitoring() {
        running = false; // Stop the monitoring thread
        if (monitorThread != null) {
            try {
                monitorThread.join(); // Wait for the thread to finish
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void resetPeakHeapUsage() {
        peakHeapUsage = 0; // Reset peak memory usage value
    }

    public static double getPeakHeapUsage() {
        return peakHeapUsage / (1024.0 * 1024.0); // Return the peak memory usage in MB
    }
}

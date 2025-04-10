package org.msse696.optimization.helper;
import org.junit.jupiter.api.Test;
import org.msse696.optimization.helper.debug.Debug;

import static org.junit.jupiter.api.Assertions.*;

class HeapMonitorTest {

    @Test
    void testStartMonitoring() throws InterruptedException {
        // Reset the peak heap usage before starting
        HeapMonitor.resetPeakHeapUsage();

        // Start heap monitoring
        HeapMonitor.startMonitoring();

        // Simulate heap usage by allocating memory
        byte[] memoryAllocation = new byte[10 * 1024 * 1024]; // Allocate ~10MB
        Thread.sleep(500); // Let monitoring thread update peak heap usage

        // Stop heap monitoring
        HeapMonitor.stopMonitoring();

        // Verify that peak heap usage is greater than 0
        double peakHeapUsage = HeapMonitor.getPeakHeapUsage();
        assertTrue(peakHeapUsage > 0, "Peak heap usage should be greater than 0");
        System.out.printf("Peak heap usage recorded: %.2f MB%n", peakHeapUsage);

        // Clean up
        memoryAllocation = null;
        System.gc(); // Force garbage collection
    }

    @Test
    void testResetPeakHeapUsage() {
        // Start monitoring
        HeapMonitor.resetPeakHeapUsage();
        HeapMonitor.startMonitoring();

        // Allocate memory to simulate heap usage
        byte[] memoryAllocation = new byte[5 * 1024 * 1024]; // Allocate ~5MB

        // Stop monitoring
        HeapMonitor.stopMonitoring();

        // Reset peak heap usage
        HeapMonitor.resetPeakHeapUsage();
        double peakHeapUsage = HeapMonitor.getPeakHeapUsage();

        // Verify that peak heap usage is reset
        assertEquals(0, peakHeapUsage, "Peak heap usage should be reset to 0");
        Debug.info("Peak heap usage after reset: " + peakHeapUsage);
    }

    @Test
    void testStopMonitoringThreadLifecycle() throws InterruptedException {
        // Reset peak heap usage
        HeapMonitor.resetPeakHeapUsage();

        // Start heap monitoring
        HeapMonitor.startMonitoring();

        // Allocate memory to simulate heap usage
        byte[] memoryAllocation = new byte[3 * 1024 * 1024]; // Allocate ~3MB
        Thread.sleep(200); // Let monitoring thread update peak heap usage

        // Stop monitoring
        HeapMonitor.stopMonitoring();
        Thread.sleep(100); // Allow time for thread cleanup

        // Ensure monitoring thread is stopped
        double peakHeapUsage = HeapMonitor.getPeakHeapUsage();
        assertTrue(peakHeapUsage > 0, "Peak heap usage should be recorded");
        System.out.printf("Peak heap usage recorded before stopping: %.2f MB%n", peakHeapUsage);

        // Clean up
        memoryAllocation = null;
        System.gc(); // Force garbage collection
    }
}

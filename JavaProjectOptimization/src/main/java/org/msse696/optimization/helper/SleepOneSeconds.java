package org.msse696.optimization.helper;

public class SleepOneSeconds {
    public SleepOneSeconds() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

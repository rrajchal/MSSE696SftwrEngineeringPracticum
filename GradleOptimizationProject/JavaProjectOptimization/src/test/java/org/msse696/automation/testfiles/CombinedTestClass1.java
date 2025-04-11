// CombinedTestClass.java
package org.msse696.automation.testfiles;

import org.msse696.optimization.helper.debug.Debug;

public class CombinedTestClass1 {

    public void inefficientArrayCopy(int size) {
        int[] src = new int[size];
        int[] dest = new int[size];
        for (int i = 0; i < size; i++) {
            dest[i] = src[i]; // Inefficient
        }
    }

    public void efficientArrayCopy(int size) {
        int[] src = new int[size];
        int[] dest = new int[size];
        System.arraycopy(src, 0, dest, 0, size); // Efficient
    }

    public String concatenateStrings(int iterations) {
        String result = "";
        for (int i = 0; i < iterations; i++) {
            result += i;
        }
        return result;
    }

    public void inefficientLoopOperation(String data) {
        for (int i = 0; i < data.length(); i++) { // length() called in each iteration
            Debug.info(data.charAt(i));
        }
    }

    public void efficientLoopOperation(String data) {
        int length = data.length();
        for (int i = 0; i < length; i++) {
            Debug.info(data.charAt(i));
        }
    }

    public void inefficientLoopObjectCreation() {
        for (int i = 0; i < 10; i++) {
            String temp = new String("temp-" + i); // Object created inside loop
            Debug.info(temp);
        }
    }

    public void efficientLoopObjectCreation() {
        String tempPrefix = "temp-";
        for (int i = 0; i < 10; i++) {
            String temp = tempPrefix + i; // No new String object created in each iteration (mostly)
            Debug.info(temp);
        }
    }

    public static class InefficientPaddingClass {
        boolean flag;
        long id;
        char grade;
        int value;
    }

    public static class EfficientPaddingClass {
        long id;
        int value;
        char grade;
        boolean flag;
    }

    public String inefficientStringConcatenation(int n) {
        String result = "";
        for (int i = 0; i < n; i++) {
            result += i; // Inefficient
        }
        return result;
    }

    public String efficientStringConcatenation(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(i); // Efficient
        }
        return sb.toString();
    }

    public void inefficientTryCatchInLoop(String[] items) {
        for (String item : items) {
            try {
                int value = Integer.parseInt(item);
                Debug.info("Value: " + value);
            } catch (NumberFormatException e) {
                Debug.error("Invalid format: " + item);
            }
        }
    }

    public void efficientTryCatchAroundLoop(String[] items) {
        try {
            for (String item : items) {
                int value = Integer.parseInt(item);
                Debug.info("Value: " + value);
            }
        } catch (NumberFormatException e) {
            Debug.error("Error parsing a number in the list.");
        }
    }
}
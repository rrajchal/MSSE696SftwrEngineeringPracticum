// CombinedTestClass.java
package org.msse696.automation.testfiles;

import org.msse696.optimization.helper.debug.Debug;

public class CombinedTestClass {

    public void inefficientArithmetic(int iterations) {
        int result = 1;
        for (int i = 0; i < iterations; i++) {
            result = result * 2; // Inefficient
        }
    }

    public void efficientBitwise(int iterations) {
        int result = 1;
        for (int i = 0; i < iterations; i++) {
            result = result << 1; // Efficient
        }
    }

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

    public void inefficientCasting(int value) {
        Integer boxedValue = (Integer) value; // Inefficient (direct assignment possible)
    }

    public void efficientCasting(int value) {
        Integer boxedValue = value; // Efficient
    }

    public void inefficientExpressionElimination(int x, int y) {
        int result1 = x * Math.abs(y) + x; // Repeated calculation
        int result2 = x * Math.abs(y) - y; // Repeated calculation
    }

    public void efficientExpressionElimination(int x, int y) {
        int absY = Math.abs(y);
        int term = x * absY;
        int result1 = term + x; // Calculated once
        int result2 = term - y; // Calculated once
    }

    public void inefficientInstanceof(Object obj) {
        try {
            String str = (String) obj; // Might throw ClassCastException
            Debug.info(str.toUpperCase());
        } catch (ClassCastException e) {
            System.err.println("Not a string");
        }
    }

    public void efficientInstanceof(Object obj) {
        if (obj instanceof String) {
            String str = (String) obj;
            Debug.info(str.toUpperCase());
        } else {
            Debug.info("Not a string");
        }
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
                System.err.println("Invalid format: " + item);
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
            System.err.println("Error parsing a number in the list.");
        }
    }
}
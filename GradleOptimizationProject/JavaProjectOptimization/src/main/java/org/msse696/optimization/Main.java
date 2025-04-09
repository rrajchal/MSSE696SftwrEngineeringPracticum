// CombinedTestClass.java
package org.msse696.optimization;

public class Main {

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
            System.out.println(str.toUpperCase());
        } catch (ClassCastException e) {
            System.err.println("Not a string");
        }
    }

    public void efficientInstanceof(Object obj) {
        if (obj instanceof String) {
            String str = (String) obj;
            System.out.println(str.toUpperCase());
        } else {
            System.out.println("Not a string");
        }
    }

    public void inefficientLoopOperation(String data) {
        for (int i = 0; i < data.length(); i++) { // length() called in each iteration
            System.out.println(data.charAt(i));
        }
    }

    public void efficientLoopOperation(String data) {
        int length = data.length();
        for (int i = 0; i < length; i++) {
            System.out.println(data.charAt(i));
        }
    }

    public void inefficientLoopObjectCreation() {
        for (int i = 0; i < 10; i++) {
            String temp = new String("temp-" + i); // Object created inside loop
            System.out.println(temp);
        }
    }

    public void efficientLoopObjectCreation() {
        String tempPrefix = "temp-";
        for (int i = 0; i < 10; i++) {
            String temp = tempPrefix + i; // No new String object created in each iteration (mostly)
            System.out.println(temp);
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
                System.out.println("Value: " + value);
            } catch (NumberFormatException e) {
                System.err.println("Invalid format: " + item);
            }
        }
    }

    public void efficientTryCatchAroundLoop(String[] items) {
        try {
            for (String item : items) {
                int value = Integer.parseInt(item);
                System.out.println("Value: " + value);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing a number in the list.");
        }
    }

    public static void main(String[] args) {
        Main test = new Main();
        test.inefficientArithmetic(100);
        test.efficientBitwise(100);
        test.inefficientArrayCopy(5);
        test.efficientArrayCopy(5);
        test.inefficientCasting(42);
        test.efficientCasting(42);
        test.inefficientExpressionElimination(5, 3);
        test.efficientExpressionElimination(5, 3);
        test.inefficientInstanceof("hello");
        test.efficientInstanceof("world");
        test.inefficientLoopOperation("testing");
        test.efficientLoopOperation("example");
        test.inefficientLoopObjectCreation();
        test.efficientLoopObjectCreation();
        System.out.println(new InefficientPaddingClass().toString());
        System.out.println(new EfficientPaddingClass().toString());
        test.inefficientStringConcatenation(10);
        test.efficientStringConcatenation(10);
        test.inefficientTryCatchInLoop(new String[]{"1", "a", "2"});
        test.efficientTryCatchAroundLoop(new String[]{"3", "b", "4"});
    }
}
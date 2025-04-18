package org.msse696.optimization.inefficient;

import org.msse696.optimization.helper.RandomObjectGenerator;
import org.msse696.optimization.helper.debug.Debug;

public class TryCatchTypeCheckingInefficient {
    public void execute(int iterations) {
        Object obj;
        for (int i = 0; i < iterations; i++) {
            obj = RandomObjectGenerator.generateRandomObject();
            // Using Try-Catch
            try {
                performOperation(obj);
            } catch (ClassCastException | NumberFormatException e) {
                //Debug.error("Invalid cast or format: " + e.getMessage());
            }
        }
    }

    private String performOperation(Object obj) throws NumberFormatException {
        Integer value = Integer.parseInt(obj.toString());
        return value.toString();
    }
}

package org.msse696.optimization.efficient;

import org.msse696.optimization.helper.RandomObjectGenerator;

public class InstanceOfTypeCheckingEfficient {
    public void execute(int iterations) {
        Object obj;
        for (int i = 0; i < iterations; i++) {
            obj = RandomObjectGenerator.generateRandomObject();
            // Using instanceof
            if (obj instanceof Integer) {
                performOperation(obj);
            }
        }
    }

    public void performOperation(Object str) {
        Integer value = Integer.parseInt(str.toString());
        value.toString();
    }
}

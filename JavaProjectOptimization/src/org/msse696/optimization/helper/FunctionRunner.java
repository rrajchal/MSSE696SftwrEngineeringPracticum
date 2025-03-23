package org.msse696.optimization.helper;

import java.util.function.Consumer;

public class FunctionRunner {
    public static void runNTimes(Runnable function, int times) {
        for (int i = 0; i < times; i++) {
            function.run();
        }
    }
}

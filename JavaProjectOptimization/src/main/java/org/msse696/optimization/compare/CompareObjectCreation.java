package org.msse696.optimization.compare;

import org.msse696.optimization.efficient.ObjectCreationEfficient;
import org.msse696.optimization.helper.ComputationUtils;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.helper.FunctionRunner;
import org.msse696.optimization.inefficient.ObjectCreationInefficient;

public class CompareObjectCreation {
    public CompareObjectCreation(boolean isEfficient, String fileName, int numberOfTimes) {
        FileDataManager fileDataManager = new FileDataManager(fileName);

        // Run and record execution times for each iteration
        FunctionRunner.runNTimes(() -> {
            long start = System.nanoTime();
            if (isEfficient) {
                compareEfficientObjectCreation(numberOfTimes);
            } else {
                compareInefficientObjectCreation(numberOfTimes);
            }
            long end = System.nanoTime();
            double executionTime = (end - start);
            // Save total execution time of numberOfTimes and there will be numberOfTimes records
            fileDataManager.appendLine(executionTime);
        }, numberOfTimes);
    }

    public void compareEfficientObjectCreation(int numberOfTimes) {
        ComputationUtils.performNComputation();
        ObjectCreationEfficient efficient = new ObjectCreationEfficient();
        efficient.processUsers(numberOfTimes);
    }

    public void compareInefficientObjectCreation(int numberOfTimes) {
        ComputationUtils.performNComputation();
        ObjectCreationInefficient inefficient = new ObjectCreationInefficient();
        inefficient.processUsers(numberOfTimes);
    }
}

package org.msse696.optimization.compare;

import org.msse696.optimization.efficient.VariableDeclarationPaddingEfficient;
import org.msse696.optimization.helper.ComputationUtils;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.helper.FunctionRunner;
import org.msse696.optimization.inefficient.VariableDeclarationPaddingInefficient;

public class ComparePadding {
    private final String name = "Mickey Mouse";
    private final double salary = 1000;
    private final int age = 50;
    private final char grade = 'A';
    private int id = 0;

    public ComparePadding(boolean isEfficient, String fileName, int numberOfTimes) {
        FileDataManager fileDataManager = new FileDataManager(fileName);

        // Run and record execution times for each iteration
        FunctionRunner.runNTimes(() -> {
            long start = System.nanoTime();
            if (isEfficient) {
                compareEfficientPadding();
            } else {
                compareInefficientPadding();
            }
            long end = System.nanoTime();
            double executionTime = (end - start);
            fileDataManager.appendLine(executionTime);
        }, numberOfTimes);
    }

    public void compareEfficientPadding() {
        ComputationUtils.performNComputation(); // Add computational cost
        VariableDeclarationPaddingEfficient goodPadding = new VariableDeclarationPaddingEfficient();
        goodPadding.setId(id++);
        goodPadding.setName(name);
        goodPadding.setAge(age);
        goodPadding.setSalary(salary);
        goodPadding.setGrade(grade);
    }

    public void compareInefficientPadding() {
        ComputationUtils.performNComputation(); // Add computational cost
        VariableDeclarationPaddingInefficient badPadding = new VariableDeclarationPaddingInefficient();
        badPadding.setId(id++);
        badPadding.setName(name);
        badPadding.setAge(age);
        badPadding.setSalary(salary);
        badPadding.setGrade(grade);
    }
}

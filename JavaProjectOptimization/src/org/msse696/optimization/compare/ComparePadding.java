package org.msse696.optimization.compare;

import org.msse696.optimization.efficient.VariableDeclarationPaddingGood;
import org.msse696.optimization.helper.ComputationUtils;
import org.msse696.optimization.helper.FileDataManager;
import org.msse696.optimization.helper.FunctionRunner;
import org.msse696.optimization.inefficient.VariableDeclarationPaddingBad;

public class ComparePadding {
    private final String name = "Mickey Mouse";
    private final double salary = 1000;
    private final int age = 50;
    private final char grade = 'A';

    int numberOfTimes = 1000000;

    public ComparePadding(boolean IsEfficient) {
        FileDataManager efficientFileDataManager = new FileDataManager(true);
        long start = 0;
        long end = 0;
        if (IsEfficient) {
            start = System.nanoTime();
            FunctionRunner.runNTimes(this::compareGoodPadding, numberOfTimes);
            end = System.nanoTime();
            efficientFileDataManager.appendLine(end - start);
        } else {
            FileDataManager inefficientFileDataManager = new FileDataManager(false);
            start = System.nanoTime();
            FunctionRunner.runNTimes(this::compareBadPadding, numberOfTimes);
            end = System.nanoTime();
            inefficientFileDataManager.appendLine(end - start);
        }
    }

    public void compareGoodPadding() {
        ComputationUtils.performNComputation();  // Add some computational cost
        VariableDeclarationPaddingGood goodPadding = new VariableDeclarationPaddingGood();
        goodPadding.setName(name);
        goodPadding.setAge(age);
        goodPadding.setSalary(salary);
        goodPadding.setGrade(grade);
    }

    public void compareBadPadding() {
        ComputationUtils.performNComputation();  // Add some computational cost
        VariableDeclarationPaddingBad badPadding = new VariableDeclarationPaddingBad();
        badPadding.setName(name);
        badPadding.setAge(age);
        badPadding.setSalary(salary);
        badPadding.setGrade(grade);
    }
}

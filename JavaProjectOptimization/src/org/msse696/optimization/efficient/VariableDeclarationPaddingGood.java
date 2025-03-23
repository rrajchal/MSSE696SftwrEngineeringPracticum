package org.msse696.optimization.efficient;

import lombok.Data;

@Data
public class VariableDeclarationPaddingGood {
    private String name;    // 8 bytes (64-bit JVM)
    private double salary;  // 8 bytes
    private int age;        // 4 bytes
    private char grade;     // 2 bytes [+ 2 bytes padding]
                            // 24 bytes Total
}

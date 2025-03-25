package org.msse696.optimization.inefficient;

import lombok.Data;

@Data
public class VariableDeclarationPaddingInefficient {
    private double id;     // 8 bytes
    private String name;   // 8 bytes (on 64-bit JVM)
    private char grade;    // 2 bytes  [+ 6 bytes padding]
    private double salary; // 8 bytes
    private int age;       // 4 bytes  [+ 4 bytes padding]
                           // 40 bytes Total
}

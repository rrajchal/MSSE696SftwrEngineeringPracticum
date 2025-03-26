package org.msse696.optimization.helper;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private String name;
    private int age;
    private String email;
    private String address;
    private double salary;
    private String phoneNumber;
}
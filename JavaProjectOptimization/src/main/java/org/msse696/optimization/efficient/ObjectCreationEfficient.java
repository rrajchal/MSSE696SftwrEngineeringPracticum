package org.msse696.optimization.efficient;

import org.msse696.optimization.helper.User;

import java.util.ArrayList;
import java.util.List;

public class ObjectCreationEfficient {
    List<User> userList = new ArrayList<>();

    public void processUsers(int numberOfIterations) {
        User user = new User("", 0, "", "", 0.0, ""); // Create the object once
        for (int i = 0; i < numberOfIterations; i++) {
            user.setName("User" + i);
            user.setAge(i);
            user.setEmail("user" + i + "@example.com");
            user.setAddress("Address" + i);
            user.setSalary(50000 + i);
            user.setPhoneNumber("123-456-" + i);
            simulateProcessing(user);
        }
    }

    private void simulateProcessing(User user) {
        userList.add(user);
    }
}

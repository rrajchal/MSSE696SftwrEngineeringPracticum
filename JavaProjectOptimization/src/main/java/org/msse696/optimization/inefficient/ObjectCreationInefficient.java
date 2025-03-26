package org.msse696.optimization.inefficient;

import org.msse696.optimization.helper.User;

import java.util.ArrayList;
import java.util.List;

public class ObjectCreationInefficient {
    List<User> userList = new ArrayList<>();
    public void processUsers(int numberOfIterations) {
        for (int i = 0; i < numberOfIterations; i++) {
            // Create Object in each loop
            User user = new User(
                "User" + i,
                i,
                "user" + i + "@example.com",
                "Address" + i,
                50000 + i,
                "123-456-" + i
            );
            simulateProcessing(user);
        }
    }

    private void simulateProcessing(User user) {
        userList.add(user);
    }
}

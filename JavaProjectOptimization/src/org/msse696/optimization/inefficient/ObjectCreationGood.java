package org.msse696.optimization.inefficient;

import org.msse696.optimization.helper.User;

import java.util.ArrayList;
import java.util.List;

public class ObjectCreationGood {
    List<User> users = new ArrayList<>();

    public void addUsers() {
        for (int i = 0; i < 1000; i++) {
            User user = new User("User" + i, i); // New object created in each iteration
            users.add(user);
        }
    }
}

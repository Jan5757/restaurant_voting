package com.github.jan5757.restaurantvoting.util;

import com.github.jan5757.restaurantvoting.to.UserTo;
import lombok.experimental.UtilityClass;
import com.github.jan5757.restaurantvoting.config.SecurityConfiguration;
import com.github.jan5757.restaurantvoting.model.Role;
import com.github.jan5757.restaurantvoting.model.User;

@UtilityClass
public class UsersUtil {

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), Role.USER);
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPassword(userTo.getPassword());
        return user;
    }

    public static User prepareToSave(User user) {
        user.setPassword(SecurityConfiguration.PASSWORD_ENCODER.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}

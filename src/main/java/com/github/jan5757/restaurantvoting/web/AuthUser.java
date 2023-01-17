package com.github.jan5757.restaurantvoting.web;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import com.github.jan5757.restaurantvoting.model.User;

public class AuthUser extends org.springframework.security.core.userdetails.User {
    @Getter
    private final User user;

    public AuthUser(@NotNull User user) {
        super(user.getEmail(), user.getPassword(), user.getRoles());
        this.user = user;
    }

    public int id() {
        return user.id();
    }

    @Override
    public String toString() {
        return "AuthUser{" + user.getId() + '[' + user.getEmail() + ']';
    }
}

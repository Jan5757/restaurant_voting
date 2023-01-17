package com.github.jan5757.restaurantvoting.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class AppException extends ResponseStatusException {

    public AppException(HttpStatus status, String message) {
        super(status, message);
    }

    @Override
    public String getMessage() {
        return getReason();
    }
}

package com.github.jan5757.restaurantvoting.error;

import org.springframework.http.HttpStatus;

public class IllegalRequestDataException extends AppException {
    public IllegalRequestDataException(String msg) {
        super(HttpStatus.UNPROCESSABLE_ENTITY, msg);
    }
}

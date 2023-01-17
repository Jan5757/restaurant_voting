package com.github.jan5757.restaurantvoting.error;

public class DataConflictException extends RuntimeException {
    public DataConflictException(String msg) {
        super(msg);
    }
}

package com.carrental.exception;

public abstract class CarRentalException extends RuntimeException {

    protected CarRentalException(String message) {
        super(message);
    }
}
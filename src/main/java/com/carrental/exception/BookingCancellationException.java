package com.carrental.exception;

public class BookingCancellationException extends RuntimeException {

    public BookingCancellationException(String message) {
        super(message);
    }
}
package com.carrental.exception;

public class UserNotFoundException extends CarRentalException {

    public UserNotFoundException(String userId) {
        super("User not found : " + userId);
    }
}
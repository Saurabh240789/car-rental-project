package com.carrental.repository;

import com.carrental.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(String userId);
}
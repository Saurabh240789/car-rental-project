package com.carrental.repository;

import com.carrental.model.User;
import com.carrental.model.enums.LicenseType;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepositoryImpl
        implements UserRepository {

    private final Map<String, User> users =
            new ConcurrentHashMap<>();

    public UserRepositoryImpl() {

        users.put(
                "USER-1",
                new User(
                        "USER-1",
                        "John",
                        LicenseType.REGULAR));

        users.put(
                "USER-2",
                new User(
                        "USER-2",
                        "Mike",
                        LicenseType.COMMERCIAL));
    }

    @Override
    public Optional<User> findById(
            String userId) {

        return Optional.ofNullable(
                users.get(userId));
    }
}
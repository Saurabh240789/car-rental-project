package com.carrental.locks;

import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class VehicleLockManager {

    private final ConcurrentHashMap<String, ReentrantLock> lockMap = new ConcurrentHashMap<>();

    public ReentrantLock getLock(String vehicleId) {
        return lockMap.computeIfAbsent(vehicleId, key -> new ReentrantLock());
    }
}
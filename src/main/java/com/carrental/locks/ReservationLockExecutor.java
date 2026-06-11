package com.carrental.locks;

import org.springframework.stereotype.Component;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

@Component
public class ReservationLockExecutor {
    private final VehicleLockManager vehicleLockManager;

    public ReservationLockExecutor(VehicleLockManager vehicleLockManager) {

        this.vehicleLockManager = vehicleLockManager;
    }

    public <T> T execute(String vehicleId, Supplier<T> action) {

        ReentrantLock lock = vehicleLockManager.getLock(vehicleId);

        lock.lock();

        try {
            return action.get();
        } finally {
            lock.unlock();
        }
    }
}
package com.carrental.rules;

import com.carrental.model.User;
import com.carrental.model.Vehicle;

public record ReservationContext(
        User user,
        Vehicle vehicle,
        int durationDays,
        int dailyMileage) {
}
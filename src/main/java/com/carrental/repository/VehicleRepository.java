package com.carrental.repository;

import com.carrental.model.Vehicle;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository {

    Optional<Vehicle> findById(String vehicleId);

    List<Vehicle> findAll();
}
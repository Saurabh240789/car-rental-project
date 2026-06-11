package com.carrental.repository;

import com.carrental.model.Vehicle;
import com.carrental.model.VehicleSeedData;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class VehicleRepositoryImpl
        implements VehicleRepository {

    private final Map<String, Vehicle> vehicles =
            new ConcurrentHashMap<>();

    public VehicleRepositoryImpl() {

        VehicleSeedData.vehicles()
                .forEach(vehicle ->
                        vehicles.put(
                                vehicle.vehicleId(),
                                vehicle));
    }

    @Override
    public Optional<Vehicle> findById(
            String vehicleId) {

        return Optional.ofNullable(
                vehicles.get(vehicleId));
    }

    @Override
    public List<Vehicle> findAll() {

        return vehicles.values()
                .stream()
                .toList();
    }
}
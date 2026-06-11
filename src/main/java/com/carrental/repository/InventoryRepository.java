package com.carrental.repository;

import com.carrental.exception.OptimisticLockingException;
import com.carrental.exception.VehicleNotFoundException;
import com.carrental.model.DailyInventory;
import com.carrental.model.InventoryKey;
import com.carrental.model.enums.VehicleCategory;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InventoryRepository {

    private final Map<InventoryKey, DailyInventory> inventoryMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void initializeInventory() {

        preloadInventory(VehicleCategory.SEDAN, LocalDate.now(), 30, 20);
        preloadInventory(VehicleCategory.VAN, LocalDate.now(), 30, 20);
        preloadInventory(VehicleCategory.SUV, LocalDate.now(), 30, 20);
        preloadInventory(VehicleCategory.PICKUP_TRUCK, LocalDate.now(), 30, 20);
    }

    private void preloadInventory(VehicleCategory category, LocalDate startDate, int numberOfDays, int inventoryCount) {

        for (int i = 0; i < numberOfDays; i++) {
            LocalDate date = startDate.plusDays(i);
            DailyInventory inventory = new DailyInventory(category, date, inventoryCount);
            inventoryMap.put(new InventoryKey(category, date), inventory);
        }
    }

    public Optional<DailyInventory> find(VehicleCategory category, LocalDate date) {

        return Optional.ofNullable(inventoryMap.get(new InventoryKey(category, date)));
    }

    public List<DailyInventory> findBetween(VehicleCategory category, LocalDate startDate, LocalDate endDate) {

        List<DailyInventory> result = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            DailyInventory inventory = inventoryMap.get(new InventoryKey(category, date));
            if (inventory != null) {
                result.add(inventory);
            }
        }

        return result;
    }

    public void save(DailyInventory inventory) {
        inventoryMap.put(new InventoryKey( inventory.getVehicleCategory(), inventory.getInventoryDate()), inventory);
    }

    public Collection<DailyInventory> findAll() {
        return inventoryMap.values();
    }

    public void updateInventory(InventoryKey key, int newCount, long expectedVersion) {
        inventoryMap.compute(key, (k, existing) -> {
            if (existing == null) {
                throw new VehicleNotFoundException("Requested Vehicle not found!");
            }

            if (existing.getVersion()
                    != expectedVersion) {
                throw new OptimisticLockingException("Version mismatch");
            }

            existing.setAvailableCount(newCount);
            existing.incrementVersion();

            return existing;
        });
    }
}
package com.carrental.model;

import com.carrental.model.enums.VehicleCategory;
import lombok.Data;

import java.time.LocalDate;

@Data
public final class DailyInventory {
    private Long inventoryId;

    private VehicleCategory vehicleCategory;

    private LocalDate inventoryDate;

    private Integer availableCount;

    private Long version;

    public DailyInventory(VehicleCategory category, LocalDate date, int availableCount) {
        this.vehicleCategory = category;
        this.inventoryDate = date;
        this.availableCount = availableCount;
        this.version = 0l;
    }

    public void decrement() {
        availableCount--;
        version++;
    }

    public void increment() {
        availableCount++;
        version++;
    }

    public void incrementVersion() {
        this.version++;
    }
}
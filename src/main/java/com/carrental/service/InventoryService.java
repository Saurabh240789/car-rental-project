package com.carrental.service;

import com.carrental.dto.GetOptionsRequest;
import com.carrental.exception.VehicleNotFoundException;
import com.carrental.model.DailyInventory;
import com.carrental.model.VehicleAvailability;
import com.carrental.model.enums.VehicleCategory;
import com.carrental.pricing.PricingContext;
import com.carrental.pricing.PricingEngine;
import com.carrental.repository.InventoryRepository;
import com.carrental.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final PricingEngine pricingEngine;

    public InventoryService(InventoryRepository inventoryRepository,
                            PricingEngine pricingEngine
    ) {
        this.inventoryRepository = inventoryRepository;
        this.pricingEngine = pricingEngine;
    }

    public List<VehicleAvailability> getOptions(GetOptionsRequest request) {
        log.info("Received Get options Request : {} ", request);
        List<VehicleAvailability> result = new ArrayList<>();
        for (VehicleCategory category : VehicleCategory.values()) {

            int available = getAvailability(category, request.startDate(), request.endDate());
            int licenceIssuedYears = Period.between(request.driverInfo().licenseIssueDate(), LocalDate.now()).getYears();
            boolean surChargeEnabled = category.equals(VehicleCategory.PICKUP_TRUCK) && licenceIssuedYears <= 3;
            boolean cleaningFeeApplicable = category.equals(VehicleCategory.VAN);
            if (available > 0) {
                result.add(new VehicleAvailability(category,
                        pricingEngine.calculate(new PricingContext(category, DateUtils.days(request.startDate(),
                                request.endDate()), surChargeEnabled, cleaningFeeApplicable, request.dailyMileage())), available));
            }
        }

        return result;

    }

    private int getAvailability(VehicleCategory category, LocalDate startDate, LocalDate endDate) {
        List<DailyInventory> rows = inventoryRepository.findBetween(category, startDate, endDate);

        long days = DateUtils.days(startDate, endDate);
        if (rows.size() != days) {
            return 0;
        }

        return rows.stream()
                .mapToInt(DailyInventory::getAvailableCount)
                .min()
                .orElse(0);
    }

    public void reserveDates(VehicleCategory category,
                             Set<LocalDate> dates) {

        List<DailyInventory> updatedRows = new ArrayList<>();
        try {
            for (LocalDate date : dates) {
                DailyInventory inventory = inventoryRepository.find(category, date).orElseThrow();
                if (inventory.getAvailableCount() <= 0) {
                    log.error("Inventory is unavailable for category : {} ", category);
                    throw new VehicleNotFoundException("Inventory unavailable");
                }

                inventory.setAvailableCount(inventory.getAvailableCount() - 1);
                inventory.incrementVersion();

                updatedRows.add(inventory);
            }

        } catch (Exception ex) {
            updatedRows.forEach(row -> {
                row.setAvailableCount(row.getAvailableCount() + 1);
                row.incrementVersion();
            });

            throw ex;
        }
    }

    public void releaseDates(VehicleCategory category, Set<LocalDate> dates) {
        for (LocalDate date : dates) {
            DailyInventory inventory = inventoryRepository.find(category, date).orElseThrow();
            inventory.setAvailableCount(inventory.getAvailableCount() + 1);
            inventory.incrementVersion();
        }
    }
}
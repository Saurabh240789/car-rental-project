package com.carrental;

import com.carrental.dto.GetOptionsRequest;
import com.carrental.model.DailyInventory;
import com.carrental.model.DriverInfo;
import com.carrental.model.VehicleAvailability;
import com.carrental.model.enums.LicenseType;
import com.carrental.model.enums.VehicleCategory;
import com.carrental.pricing.PricingContext;
import com.carrental.pricing.PricingEngine;
import com.carrental.repository.InventoryRepository;
import com.carrental.service.InventoryService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private PricingEngine pricingEngine;

    @InjectMocks
    private InventoryService inventoryService;

    //@Test
    void shouldReturnAvailableVehicleOptions() {

        DriverInfo driverInfo =
                new DriverInfo(
                        LicenseType.REGULAR,
                        LocalDate.now().minusYears(4));

        GetOptionsRequest request =
                new GetOptionsRequest(
                        LocalDate.of(2026, 6, 20),
                        LocalDate.of(2026, 6, 22),
                        100,
                        driverInfo);

        DailyInventory day1 =
                new DailyInventory(
                        VehicleCategory.SUV,
                        LocalDate.of(2026, 6, 20),
                        5);

        DailyInventory day2 =
                new DailyInventory(
                        VehicleCategory.SUV,
                        LocalDate.of(2026, 6, 21),
                        3);

        when(inventoryRepository.findBetween(
                any(VehicleCategory.class),
                any(),
                any()))
                .thenReturn(Collections.emptyList());

        when(inventoryRepository.findBetween(
                eq(VehicleCategory.SUV),
                any(),
                any()))
                .thenReturn(List.of(day1, day2));

        when(pricingEngine.calculate(any()))
                .thenReturn(BigDecimal.valueOf(2500.0));

        List<VehicleAvailability> result =
                inventoryService.getOptions(request);

        assertEquals(1, result.size());

        VehicleAvailability suv = result.get(0);

        assertEquals(
                VehicleCategory.SUV,
                suv.category());

        assertEquals(
                3,
                suv.availableCount());

        verify(pricingEngine, times(1))
                .calculate(any(PricingContext.class));
    }
}
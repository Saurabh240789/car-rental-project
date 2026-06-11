package com.carrental;


import com.carrental.dto.ReserveRequest;

import com.carrental.exception.VehicleNotFoundException;
import com.carrental.model.DailyInventory;
import com.carrental.model.DriverInfo;
import com.carrental.model.Reservation;
import com.carrental.model.enums.LicenseType;
import com.carrental.model.enums.VehicleCategory;
import com.carrental.pricing.PricingContext;
import com.carrental.pricing.PricingEngine;
import com.carrental.repository.InventoryRepository;
import com.carrental.repository.ReservationRepository;
import com.carrental.service.BookingService;
import com.carrental.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookingServiceTest {

    private InventoryRepository inventoryRepository;
    private ReservationRepository reservationRepository;
    private PricingEngine pricingEngine;
    private InventoryService inventoryService;
    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        inventoryRepository = mock(InventoryRepository.class);
        reservationRepository = mock(ReservationRepository.class);
        pricingEngine = mock(PricingEngine.class);
        inventoryService = mock(InventoryService.class);

        bookingService = new BookingService(
                inventoryRepository,
                reservationRepository,
                pricingEngine,
                inventoryService
        );
    }

    @Test
    void shouldCreateReservationAndDecrementInventory() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = startDate.plusDays(1);

        ReserveRequest request = new ReserveRequest(
                VehicleCategory.SEDAN,
                new DriverInfo(LicenseType.REGULAR, LocalDate.now().minusYears(5)),
                startDate,
                endDate,
                100
        );

        DailyInventory dayOne = new DailyInventory(VehicleCategory.SEDAN, startDate, 2);
        DailyInventory dayTwo = new DailyInventory(VehicleCategory.SEDAN, endDate, 3);

        when(inventoryRepository.findBetween(VehicleCategory.SEDAN, startDate, endDate))
                .thenReturn(List.of(dayOne, dayTwo));
        when(pricingEngine.calculate(any(PricingContext.class)))
                .thenReturn(BigDecimal.valueOf(250));

        Reservation reservation = bookingService.reserve(request);

        assertNotNull(reservation);
        assertEquals(VehicleCategory.SEDAN, reservation.getVehicleCategory());
        assertEquals(startDate, reservation.getStartDate());
        assertEquals(endDate, reservation.getEndDate());
        assertEquals(BigDecimal.valueOf(250), reservation.getAmount());

        assertEquals(1, dayOne.getAvailableCount());
        assertEquals(2, dayTwo.getAvailableCount());
        assertEquals(1L, dayOne.getVersion());
        assertEquals(1L, dayTwo.getVersion());

        verify(inventoryRepository).save(dayOne);
        verify(inventoryRepository).save(dayTwo);
        verify(reservationRepository).save(reservation);
    }

    @Test
    void shouldThrowVehicleNotFoundException_whenNoInventoryRowsExist() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = startDate.plusDays(1);

        ReserveRequest request = new ReserveRequest(
                VehicleCategory.SUV,
                new DriverInfo(LicenseType.REGULAR, LocalDate.now().minusYears(5)),
                startDate,
                endDate,
                50
        );

        when(inventoryRepository.findBetween(VehicleCategory.SUV, startDate, endDate))
                .thenReturn(List.of());

        assertThrows(VehicleNotFoundException.class, () -> bookingService.reserve(request));

        verify(reservationRepository, never()).save(any());
        verify(pricingEngine, never()).calculate(any());
    }

    @Test
    void reserve_shouldThrowRuntimeException_whenAnyInventoryRowHasNoAvailability() {
        LocalDate startDate = LocalDate.now().plusDays(1);

        ReserveRequest request = new ReserveRequest(
                VehicleCategory.VAN,
                new DriverInfo(LicenseType.REGULAR, LocalDate.now().minusYears(5)),
                startDate,
                startDate,
                80
        );

        DailyInventory unavailableInventory = new DailyInventory(VehicleCategory.VAN, startDate, 0);

        when(inventoryRepository.findBetween(VehicleCategory.VAN, startDate, startDate))
                .thenReturn(List.of(unavailableInventory));

        assertThrows(RuntimeException.class, () -> bookingService.reserve(request));

        verify(inventoryRepository, never()).save(any());
        verify(reservationRepository, never()).save(any());
        verify(pricingEngine, never()).calculate(any());
    }
}
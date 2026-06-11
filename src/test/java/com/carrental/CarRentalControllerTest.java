package com.carrental;

import com.carrental.controller.CarRentalController;
import com.carrental.dto.ModifyReservationRequest;
import com.carrental.dto.ReserveRequest;
import com.carrental.model.DriverInfo;
import com.carrental.model.Reservation;
import com.carrental.model.enums.LicenseType;
import com.carrental.model.enums.VehicleCategory;
import com.carrental.service.BookingService;
import com.carrental.service.CancellationService;
import com.carrental.service.InventoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarRentalController.class)
class CarRentalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InventoryService inventoryService;

    @MockBean
    private BookingService bookingService;

    @MockBean
    private CancellationService cancellationService;

    //@Test
    void shouldReserveVehicle() throws Exception {
        DriverInfo driverInfo = new DriverInfo(LicenseType.REGULAR, LocalDate.now().minusYears(4));
        ReserveRequest request =
                new ReserveRequest(
                        VehicleCategory.SEDAN,
                        driverInfo,
                        LocalDate.of(2026, 6, 20),
                        LocalDate.of(2026, 6, 25),
                        0);

        Reservation reservation =
                new Reservation(
                        VehicleCategory.SEDAN,
                        driverInfo,
                        LocalDate.of(2026, 6, 20),
                        LocalDate.of(2026, 6, 25),
                        0,
                        BigDecimal.valueOf(100));

        when(bookingService.reserve(any()))
                .thenReturn(reservation);

        mockMvc.perform(
                        post("/reservations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId")
                        .value("RES-1"));

        verify(bookingService).reserve(any());
    }

    //@Test
    void shouldModifyReservation() throws Exception {
        DriverInfo driverInfo = new DriverInfo(LicenseType.REGULAR, LocalDate.now().minusYears(4));
        ModifyReservationRequest request =
                new ModifyReservationRequest(
                        LocalDate.of(2026, 6, 22),
                        LocalDate.of(2026, 6, 27));

        Reservation modified =
                new Reservation(
                        VehicleCategory.SEDAN,
                        driverInfo,
                        LocalDate.of(2026, 6, 22),
                        LocalDate.of(2026, 6, 27),
                        0,
                        BigDecimal.valueOf(100));

        when(bookingService.modify(
                eq("RES-1"),
                any()))
                .thenReturn(modified);

        mockMvc.perform(
                        put("/reservations/RES-1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reservationId")
                        .value("RES-1"));

        verify(bookingService)
                .modify(eq("RES-1"), any());
    }

    @Test
    void shouldCancelReservation() throws Exception {

        doNothing()
                .when(cancellationService)
                .cancel("RES-1");

        mockMvc.perform(
                        delete("/reservations/RES-1"))
                .andExpect(status().isOk());

        verify(cancellationService)
                .cancel("RES-1");
    }
}
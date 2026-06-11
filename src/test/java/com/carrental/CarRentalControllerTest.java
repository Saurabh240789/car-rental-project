package com.carrental;

import com.carrental.controller.CarRentalController;
import com.carrental.dto.ReservationResponse;
import com.carrental.exception.VehicleAlreadyReservedException;
import com.carrental.exception.VehicleNotFoundException;
import com.carrental.model.enums.ReservationStatus;
import com.carrental.service.CarRentalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarRentalController.class)
class CarRentalControllerTest {

    private static final String VALID_REQUEST =
            """
                    {
                      "vehicleId":"SUV-1",
                      "userId":"USER-1",
                      "startDate":"2026-06-10",
                      "endDate":"2026-06-15",
                      "dailyMileage":100
                    }
                    """;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CarRentalService service;

    @Test
    void shouldReserveVehicle() throws Exception {

        ReservationResponse response =
                new ReservationResponse(
                        "RES-1",
                        "SUV-1",
                        "USER-1",
                        LocalDate.of(2026, 6, 10),
                        LocalDate.of(2026, 6, 15),
                        100,
                        325.0,
                        ReservationStatus.ACTIVE);

        when(service.reserve(any()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/reservations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(VALID_REQUEST))
                .andExpect(status().isOk());
    }

    @Test
    void shouldFailValidation() throws Exception {

        mockMvc.perform(
                        post("/reservations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404() throws Exception {

        when(service.reserve(any()))
                .thenThrow(
                        new VehicleNotFoundException(
                                "SUV-100"));

        mockMvc.perform(
                        post("/reservations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(VALID_REQUEST))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn409() throws Exception {

        when(service.reserve(any()))
                .thenThrow(
                        new VehicleAlreadyReservedException(
                                "SUV-1"));

        mockMvc.perform(
                        post("/reservations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(VALID_REQUEST))
                .andExpect(status().isConflict());
    }
}
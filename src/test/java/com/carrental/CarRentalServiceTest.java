package com.carrental;

import com.carrental.dto.ReservationResponse;
import com.carrental.dto.ReserveRequest;
import com.carrental.exception.UserNotFoundException;
import com.carrental.exception.VehicleAlreadyReservedException;
import com.carrental.exception.VehicleNotFoundException;
import com.carrental.locks.ReservationLockExecutor;
import com.carrental.model.User;
import com.carrental.model.Vehicle;
import com.carrental.model.enums.LicenseType;
import com.carrental.model.enums.VehicleCategory;
import com.carrental.pricing.PricingEngine;
import com.carrental.repository.ReservationRepository;
import com.carrental.repository.UserRepository;
import com.carrental.repository.VehicleRepository;
import com.carrental.rules.RuleEngine;
import com.carrental.service.CarRentalServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class CarRentalServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private PricingEngine pricingEngine;
    @Mock
    private RuleEngine ruleEngine;
    @Mock
    private ReservationLockExecutor lockExecutor;

    @InjectMocks
    private CarRentalServiceImpl service;

    private Vehicle vehicle;
    private User user;
    private ReserveRequest reserveRequest;

    @BeforeEach
    void setup() {

        vehicle = new Vehicle("SUV-1", VehicleCategory.SUV);
        user = new User("USER-1", "John", LicenseType.REGULAR);

        reserveRequest = new ReserveRequest(
                "SUV-1",
                "USER-1",
                LocalDate.now(),
                LocalDate.now().plusDays(5),
                100
        );

        given(lockExecutor.execute(any(), any()))
                .willAnswer(invocation -> {
                    Supplier<?> supplier = invocation.getArgument(1);
                    return supplier.get();
                });
    }

    @Test
    void shouldReserveVehicleSuccessfully() {

        given(vehicleRepository.findById("SUV-1"))
                .willReturn(Optional.of(vehicle));

        given(userRepository.findById("USER-1"))
                .willReturn(Optional.of(user));

        given(pricingEngine.calculate(any()))
                .willReturn(500.0);

        given(reservationRepository.reserveVehicle("SUV-1", "USER-1"))
                .willReturn(true);

        ReservationResponse response = service.reserve(reserveRequest);

        assertNotNull(response.reservationId());
        assertEquals("SUV-1", response.vehicleId());
        assertEquals("USER-1", response.userId());

        verify(ruleEngine).validate(any());
        verify(pricingEngine).calculate(any());
        verify(reservationRepository).reserveVehicle("SUV-1", "USER-1");
    }

    @Test
    void shouldThrowVehicleNotFound() {

        given(vehicleRepository.findById(any()))
                .willReturn(Optional.empty());

        assertThrows(
                VehicleNotFoundException.class,
                () -> service.reserve(reserveRequest)
        );
    }

    @Test
    void shouldThrowUserNotFound() {

        given(vehicleRepository.findById(any()))
                .willReturn(Optional.of(vehicle));

        given(userRepository.findById(any()))
                .willReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> service.reserve(reserveRequest)
        );
    }

    @Test
    void shouldThrowVehicleAlreadyReserved() {

        given(vehicleRepository.findById(any()))
                .willReturn(Optional.of(vehicle));

        given(userRepository.findById(any()))
                .willReturn(Optional.of(user));

        given(pricingEngine.calculate(any()))
                .willReturn(500.0);

        given(reservationRepository.reserveVehicle(any(), any()))
                .willReturn(false);

        assertThrows(
                VehicleAlreadyReservedException.class,
                () -> service.reserve(reserveRequest)
        );
    }
}
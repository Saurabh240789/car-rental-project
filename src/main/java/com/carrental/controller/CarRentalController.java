package com.carrental.controller;

import com.carrental.dto.*;
import com.carrental.model.enums.LicenseType;
import com.carrental.service.CarRentalService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Car Rental APIs", description = "Reservation operations")
@RestController
@RequestMapping("/reservations")
public class CarRentalController {

    private final CarRentalService service;

    public CarRentalController(CarRentalService service) {
        this.service = service;
    }

    @PostMapping
    public ReservationResponse reserve(@Valid @RequestBody ReserveRequest request) {
        return service.reserve(request);
    }

    @PutMapping("/{reservationId}")
    public ReservationResponse modify(@PathVariable String reservationId, @Valid @RequestBody ModifyReservationRequest request) {
        return service.modify(reservationId, request);
    }

    @DeleteMapping("/{reservationId}")
    public void cancel(@PathVariable String reservationId) {
        service.cancel(reservationId);
    }

    @GetMapping("/options")
    public List<ReservationOptionResponse> getOptions(@RequestParam int durationDays,
                                                      @RequestParam int dailyMileage,
                                                      @RequestParam LicenseType licenseType) {
        return service.getOptions(
                new GetOptionsRequest(
                        durationDays,
                        dailyMileage,
                        licenseType));
    }
}
package com.carrental.controller;

import com.carrental.dto.GetOptionsRequest;
import com.carrental.dto.ModifyReservationRequest;
import com.carrental.dto.ReserveRequest;
import com.carrental.model.Reservation;
import com.carrental.model.VehicleAvailability;
import com.carrental.service.BookingService;
import com.carrental.service.CancellationService;
import com.carrental.service.InventoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Car Rental APIs", description = "Reservation operations")
@RestController
@RequestMapping("/reservations")
public class CarRentalController {

    private final InventoryService inventoryService;
    private final BookingService bookingService;
    private final CancellationService cancellationService;

    public CarRentalController(InventoryService inventoryService, BookingService bookingService, CancellationService cancellationService) {
        this.inventoryService = inventoryService;
        this.bookingService = bookingService;
        this.cancellationService = cancellationService;
    }


    @PostMapping
    public Reservation reserve(@Valid @RequestBody ReserveRequest request) {
        return bookingService.reserve(request);
    }

    @PutMapping("/{reservationId}")
    public Reservation modify(@PathVariable String reservationId, @Valid @RequestBody ModifyReservationRequest request) {
        return bookingService.modify(reservationId, request);
    }

    @DeleteMapping("/{reservationId}")
    public void cancel(@PathVariable String reservationId) {
        cancellationService.cancel(reservationId);
    }

    @PostMapping("/options")
    public List<VehicleAvailability> getOptions(@Valid @RequestBody GetOptionsRequest getOptionRequest) {
        return inventoryService.getOptions(getOptionRequest);
    }
}
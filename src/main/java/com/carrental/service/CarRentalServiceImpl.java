package com.carrental.service;

import com.carrental.dto.*;
import com.carrental.exception.UserNotFoundException;
import com.carrental.exception.VehicleAlreadyReservedException;
import com.carrental.exception.VehicleNotFoundException;
import com.carrental.locks.ReservationLockExecutor;
import com.carrental.model.Reservation;
import com.carrental.model.User;
import com.carrental.model.Vehicle;
import com.carrental.model.enums.VehicleCategory;
import com.carrental.pricing.PricingContext;
import com.carrental.pricing.PricingEngine;
import com.carrental.repository.ReservationRepository;
import com.carrental.repository.UserRepository;
import com.carrental.repository.VehicleRepository;
import com.carrental.rules.ReservationContext;
import com.carrental.rules.RuleEngine;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarRentalServiceImpl implements CarRentalService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final PricingEngine pricingEngine;
    private final RuleEngine ruleEngine;
    private final ReservationLockExecutor lockExecutor;

    public CarRentalServiceImpl(
            VehicleRepository vehicleRepository,
            UserRepository userRepository,
            ReservationRepository reservationRepository,
            PricingEngine pricingEngine,
            RuleEngine ruleEngine,
            ReservationLockExecutor lockExecutor
    ) {
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.reservationRepository = reservationRepository;
        this.pricingEngine = pricingEngine;
        this.ruleEngine = ruleEngine;
        this.lockExecutor = lockExecutor;
    }

    @Override
    public ReservationResponse reserve(ReserveRequest request) {

        return lockExecutor.execute(request.vehicleId(), () -> {

            Vehicle vehicle =
                    vehicleRepository.findById(request.vehicleId())
                            .orElseThrow(() ->
                                    new VehicleNotFoundException(request.vehicleId()));

            User user =
                    userRepository.findById(request.userId())
                            .orElseThrow(() ->
                                    new UserNotFoundException(request.userId()));

            int days = (int) ChronoUnit.DAYS.between(
                    request.startDate(),
                    request.endDate()
            ) + 1;

            ReservationContext ruleContext =
                    new ReservationContext(user, vehicle, days, request.dailyMileage());

            ruleEngine.validate(ruleContext);

            PricingContext pricingContext =
                    new PricingContext(vehicle.category(), days, request.dailyMileage());

            double amount = pricingEngine.calculate(pricingContext);

            Reservation reservation =
                    new Reservation(
                            vehicle.vehicleId(),
                            user.userId(),
                            request.startDate(),
                            request.endDate(),
                            request.dailyMileage(),
                            amount
                    );

            boolean reserved =
                    reservationRepository.reserveVehicle(
                            reservation.getVehicleId(),
                            reservation.getUserId()
                    );

            if (!reserved) {
                throw new VehicleAlreadyReservedException(vehicle.vehicleId());
            }

            reservationRepository.save(reservation);

            return toResponse(reservation);
        });
    }

    @Override
    public ReservationResponse modify(String reservationId, ModifyReservationRequest request) {

        return lockExecutor.execute(request.vehicleId(), () -> {

            Reservation reservation =
                    reservationRepository.findById(request.reservationId())
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Reservation not found: " + request.reservationId()));

            Vehicle vehicle =
                    vehicleRepository.findById(request.vehicleId())
                            .orElseThrow(() ->
                                    new VehicleNotFoundException(request.vehicleId()));

            User user =
                    userRepository.findById(request.userId())
                            .orElseThrow(() ->
                                    new UserNotFoundException(request.userId()));

            int days = (int) ChronoUnit.DAYS.between(
                    request.startDate(),
                    request.endDate()
            ) + 1;

            ReservationContext ruleContext =
                    new ReservationContext(user, vehicle, days, request.dailyMileage());

            ruleEngine.validate(ruleContext);

            PricingContext pricingContext =
                    new PricingContext(vehicle.category(), days, request.dailyMileage());

            double newAmount = pricingEngine.calculate(pricingContext);

            reservation.modify(
                    request.startDate(),
                    request.endDate(),
                    request.dailyMileage()
            );

            reservation.updateAmount(newAmount);

            reservationRepository.update(reservation);

            return toResponse(reservation);
        });
    }


    @Override
    public void cancel(String reservationId) {
        Reservation reservation =
                reservationRepository.findById(reservationId)
                        .orElseThrow(() ->
                                new IllegalArgumentException("Reservation not found"));

        reservation.cancel();

        reservationRepository.update(reservation);
    }

    @Override
    public List<ReservationOptionResponse> getOptions(GetOptionsRequest request) {

        User user =
                userRepository.findById("TEMP_USER") // replace if userId exists
                        .orElseThrow(() ->
                                new UserNotFoundException("TEMP_USER"));

        return List.of(VehicleCategory.values())
                .stream()
                .map(category -> {

                    Vehicle vehicle = new Vehicle("TEMP", category);

                    ReservationContext context =
                            new ReservationContext(
                                    user,
                                    vehicle,
                                    request.durationDays(),
                                    request.dailyMileage()
                            );

                    boolean eligible;

                    try {
                        ruleEngine.validate(context);
                        eligible = true;
                    } catch (Exception ex) {
                        eligible = false;
                    }

                    double price = 0.0;

                    if (eligible) {
                        PricingContext pricingContext =
                                new PricingContext(
                                        category,
                                        request.durationDays(),
                                        request.dailyMileage()
                                );

                        price = pricingEngine.calculate(pricingContext);
                    }

                    return new ReservationOptionResponse(
                            category,
                            price,
                            eligible
                    );
                })
                .sorted((a, b) -> Double.compare(a.totalAmount(), b.totalAmount()))
                .collect(Collectors.toList());
    }

    private ReservationResponse toResponse(Reservation r) {

        return new ReservationResponse(
                r.getReservationId(),
                r.getVehicleId(),
                r.getUserId(),
                r.getStartDate(),
                r.getEndDate(),
                r.getDailyMileage(),
                r.getAmount(),
                r.getStatus()
        );
    }
}
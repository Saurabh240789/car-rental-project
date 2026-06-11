package com.carrental.service;

import com.carrental.dto.*;

import java.util.List;

public interface CarRentalService {

    ReservationResponse reserve(
            ReserveRequest request);

    ReservationResponse modify(
            String reservationId,
            ModifyReservationRequest request);

    void cancel(
            String reservationId);

    List<ReservationOptionResponse>
    getOptions(
            GetOptionsRequest request);
}
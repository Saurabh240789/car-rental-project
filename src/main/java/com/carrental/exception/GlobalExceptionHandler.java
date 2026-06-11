package com.carrental.exception;

import com.carrental.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(VehicleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleVehicleNotFound(VehicleNotFoundException ex, HttpServletRequest request) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("VEHICLE_NOT_FOUND", ex.getMessage(),
                        request.getRequestURI(),
                        List.of(),
                        Instant.now()));
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleReservationNotFound(ReservationNotFoundException ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        "RESERVATION_NOT_FOUND",
                        ex.getMessage(),
                        request.getRequestURI(),
                        List.of(),
                        Instant.now()));
    }

    @ExceptionHandler(VehicleAlreadyReservedException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyReserved(VehicleAlreadyReservedException ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(
                        "VEHICLE_ALREADY_RESERVED",
                        ex.getMessage(),
                        request.getRequestURI(),
                        List.of(),
                        Instant.now()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<String> details = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(
                        "VALIDATION_ERROR",
                        "Request validation failed",
                        request.getRequestURI(),
                        details,
                        Instant.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        "INTERNAL_SERVER_ERROR",
                        ex.getMessage(),
                        request.getRequestURI(),
                        List.of(),
                        Instant.now()));
    }

    @ExceptionHandler(BookingCancellationException.class)
    public ResponseEntity<ErrorResponse> handelCancellationException(Exception ex, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        "INTERNAL_SERVER_ERROR",
                        ex.getMessage(),
                        request.getRequestURI(),
                        List.of(),
                        Instant.now()));
    }

}
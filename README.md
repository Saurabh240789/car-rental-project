# Car Rental Project

A simple Spring Boot-based car rental reservation system.  
It allows users to check available vehicles, reserve a vehicle, modify an existing reservation, and cancel a reservation.

---

## Tech Stack

- Java 17
- Spring Boot 3.3.0
- Spring Web
- Spring Validation
- Lombok
- SpringDoc OpenAPI / Swagger UI
- JUnit 5 and Mockito for testing
- Maven

---

## Core Functionalities

### 1. Check Vehicle Availability

Users can search for available vehicles for a given date range.

The system checks inventory across all supported vehicle categories and returns only vehicles that are available for the complete requested period.

Supported vehicle categories:

- `SEDAN`
- `SUV`
- `VAN`
- `PICKUP_TRUCK`

Endpoint: http POST /reservations/options


---

### 2. Reserve a Vehicle

Users can reserve a vehicle by providing:

- Vehicle category
- Driver information
- Start date
- End date
- Expected daily mileage

When a reservation is created:

- Inventory count is reduced for each reserved date.
- Pricing is calculated based on vehicle category, rental duration, mileage, and applicable extra charges.
- A reservation record is saved with status `RESERVED`.

Endpoint: http POST /reservations


---

### 3. Modify a Reservation

Users can modify the start and end dates of an existing reservation.

When dates are changed:

- Dates no longer needed are released back to inventory.
- Newly added dates are reserved from inventory.
- If reserving the new dates fails, the system attempts to roll back the released dates.

Endpoint: http POST /reservations/modify


---

### 4. Cancel a Reservation

Users can cancel an existing reservation.

When a reservation is cancelled:

- Reserved inventory is released for the reservation date range.
- Reservation status is updated to `CANCELLED`.

Endpoint: http POST /reservations/cancel


---

## Pricing Overview

The project uses a pricing engine with separate pricing rules for each vehicle category.

Base pricing rules are available for:

- Sedan
- SUV
- Van
- Pickup Truck

Additional pricing rules include:

- Surcharge for pickup trucks when the driver's license was issued recently
- Cleaning fee for vans

The pricing system is rule-based, making it easier to add or update pricing logic later.

---

## Inventory Management

Inventory is tracked per vehicle category and date.

Each inventory record contains:

- Vehicle category
- Inventory date
- Available count
- Version number

The version number is used to help detect concurrent inventory updates.

During reservation:

- Inventory is checked for the full date range.
- If inventory is available, the available count is reduced.
- If inventory is missing or unavailable, the reservation fails.

---

## Validation

The application validates incoming API requests.

Common validations include:

- Required vehicle category
- Required driver information
- Required start date
- Required end date
- Non-negative daily mileage
- Valid date range

Invalid requests return a validation error response.

---

## Error Handling

The project includes centralized exception handling.

Example error scenarios:

- Vehicle not found
- Reservation not found
- Vehicle already reserved
- Invalid request body
- Unexpected server error

Error responses follow a common structure containing:

- Error code
- Message
- Request path
- Details
- Timestamp

---

## Running the Application

Use Maven Wrapper to start the application:

bash ./mvnw spring-boot:run


The application starts on the default Spring Boot port:
 [http://localhost:8080](http://localhost:8080)


---

## API Documentation

Swagger UI is available through SpringDoc OpenAPI.

After starting the application, open:
http://localhost:8080/swagger-ui/index.html


---

## Notes

This project currently uses in-memory repositories, so data is reset when the application restarts.

It is suitable as a learning/demo project for:

- Spring Boot REST APIs
- Layered architecture
- Validation
- Exception handling
- Unit and controller testing
- Rule-based pricing logic

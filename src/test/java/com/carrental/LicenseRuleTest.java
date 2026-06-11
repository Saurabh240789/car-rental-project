package com.carrental;

import com.carrental.exception.RuleViolationException;
import com.carrental.model.User;
import com.carrental.model.Vehicle;
import com.carrental.model.enums.LicenseType;
import com.carrental.model.enums.VehicleCategory;
import com.carrental.rules.LicenseRule;
import com.carrental.rules.ReservationContext;
import com.carrental.rules.RuleEngine;
import com.carrental.rules.RuleResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LicenseRuleTest {

    private LicenseRule rule;
    private RuleEngine engine;

    private User regularUser;
    private User commercialUser;
    private Vehicle pickupTruck;

    @BeforeEach
    void setUp() {

        rule = new LicenseRule();

        engine = new RuleEngine(List.of(rule));

        regularUser = new User(
                "U1",
                "John",
                LicenseType.REGULAR);

        commercialUser = new User(
                "U2",
                "Mike",
                LicenseType.COMMERCIAL);

        pickupTruck = new Vehicle(
                "P1",
                VehicleCategory.PICKUP_TRUCK);
    }
    
    @Test
    void shouldRejectRegularLicenseForPickupTruck() {
        RuleResult result =
                rule.validate(
                        new ReservationContext(
                                regularUser,
                                pickupTruck,
                                5,
                                100));

        assertAll(
                () -> assertFalse(result.success()),
                () -> assertTrue(result.message().toLowerCase()
                        .contains("license"))
        );
    }

    @Test
    void shouldThrowExceptionWhenRuleFails() {
        RuleViolationException ex =
                assertThrows(
                        RuleViolationException.class,
                        () -> engine.validate(
                                new ReservationContext(
                                        regularUser,
                                        pickupTruck,
                                        5,
                                        100)));

        assertNotNull(ex.getMessage());
    }

    @Test
    void shouldPassValidationWhenRulesAreSatisfied() {

        assertDoesNotThrow(() ->
                engine.validate(
                        new ReservationContext(
                                commercialUser,
                                pickupTruck,
                                5,
                                100)));
    }
}
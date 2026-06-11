package com.carrental;

import com.carrental.model.enums.VehicleCategory;
import com.carrental.pricing.PricingEngine;
import com.carrental.pricing.SedanPricingStrategy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SedanPricingStrategyTest {

    private final SedanPricingStrategy strategy = new SedanPricingStrategy();

    @Test
    void shouldCalculateShortTermPrice() {

        double amount =
                strategy.calculatePrice(
                        5,
                        100);

        assertEquals(
                100,
                amount);
    }

    @Test
    void shouldCalculateLongTermPrice() {

        double amount =
                strategy.calculatePrice(
                        15,
                        100);

        assertEquals(
                225,
                amount);
    }

    @Test
    void shouldThrowWhenStrategyMissing() {

        PricingEngine engine =
                new PricingEngine(
                        List.of());

        engine.initialize();

        assertThrows(
                IllegalArgumentException.class,
                () -> engine.calculatePrice(
                        VehicleCategory.SUV,
                        5,
                        100));
    }
}
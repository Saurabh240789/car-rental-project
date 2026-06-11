package com.carrental.config;

import com.carrental.pricing.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class config {
    @Bean
    public PricingEngine pricingEngine(PricingProperties properties) {

        return new PricingEngine(List.of(
                new SedanShortTripRule(properties),
                new SedanLongTripRule(properties),
                new SuvPricingRule(properties),
                new VanPricingRule(properties),
                new PickupTruckPricingRule(properties)
        ));
    }
}

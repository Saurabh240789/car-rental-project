package com.carrental.config;

import com.carrental.pricing.PricingEngine;
import com.carrental.pricing.base.PickupTruckPricingRule;
import com.carrental.pricing.base.SedanLongTripRule;
import com.carrental.pricing.base.SedanShortTripRule;
import com.carrental.pricing.base.SuvPricingRule;
import com.carrental.pricing.base.VanPricingRule;
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

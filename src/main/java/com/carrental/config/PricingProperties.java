package com.carrental.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "pricing")
public class PricingProperties {

    private Sedan sedan;
    private Suv suv;
    private Van van;
    private Pickup pickup;

    @Data
    public static class Sedan {
        private double shortDayRate;
        private double longDayRate;
        private int longDayThreshold;
    }

    @Data
    public static class Suv {
        private double baseDayRate;
        private double mileageRate;
    }

    @Data
    public static class Van {
        private double baseDayRate;
    }

    @Data
    public static class Pickup {
        private double baseDayRate;
    }
}
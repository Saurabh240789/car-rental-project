package com.carrental.rules;

import com.carrental.model.enums.LicenseType;
import com.carrental.model.enums.VehicleCategory;

public class LicenseRule implements Rule {

    private static final String PICKUP_LICENSE_ERROR =
            "Commercial license required for pickup truck";

    @Override
    public RuleResult validate(ReservationContext context) {

        if (isPickupTruck(context)
                && isNotCommercialLicense(context)) {

            return new RuleResult(false, PICKUP_LICENSE_ERROR);
        }

        return new RuleResult(true, "");
    }

    private boolean isPickupTruck(
            ReservationContext context) {

        return context.vehicle()
                .category()
                == VehicleCategory.PICKUP_TRUCK;
    }

    private boolean isNotCommercialLicense(
            ReservationContext context) {

        return context.user()
                .licenseType()
                != LicenseType.COMMERCIAL;
    }
}
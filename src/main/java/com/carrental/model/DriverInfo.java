package com.carrental.model;

import com.carrental.model.enums.LicenseType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record DriverInfo(
        @NotNull
        LicenseType licenseType,

        @NotNull
        LocalDate licenseIssueDate) {
}

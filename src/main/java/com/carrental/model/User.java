package com.carrental.model;

import com.carrental.model.enums.LicenseType;

public record User(

        String userId,

        String name,

        LicenseType licenseType) {
}
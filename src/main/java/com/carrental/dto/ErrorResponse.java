package com.carrental.dto;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(

        String errorCode,

        String message,

        String path,

        List<String> details,

        Instant timestamp) {
}
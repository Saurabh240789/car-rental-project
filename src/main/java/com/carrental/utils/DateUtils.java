package com.carrental.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashSet;
import java.util.Set;

public class DateUtils {

    public static long days(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end) + 1;
    }

    public static Set<LocalDate> getDates(LocalDate startDate, LocalDate endDate) {

        Set<LocalDate> dates = new LinkedHashSet<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dates.add(date);
        }

        return dates;
    }
}

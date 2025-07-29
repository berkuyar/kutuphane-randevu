package com.uyarberk.kutuphane_randevu.exception;

public class AppointmentPeriodNotFoundException extends RuntimeException {
    public AppointmentPeriodNotFoundException(String message) {
        super(message);
    }
}
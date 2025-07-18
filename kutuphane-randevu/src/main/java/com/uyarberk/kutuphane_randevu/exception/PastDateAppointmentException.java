package com.uyarberk.kutuphane_randevu.exception;

public class PastDateAppointmentException extends RuntimeException {

    public PastDateAppointmentException(String message) {
        super(message);
    }

}

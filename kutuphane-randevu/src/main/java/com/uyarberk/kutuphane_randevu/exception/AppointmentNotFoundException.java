package com.uyarberk.kutuphane_randevu.exception;

public class AppointmentNotFoundException extends RuntimeException{

    public AppointmentNotFoundException(String message){
        super(message);
    }
}

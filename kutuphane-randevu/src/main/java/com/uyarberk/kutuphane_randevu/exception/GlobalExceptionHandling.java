package com.uyarberk.kutuphane_randevu.exception;

import com.uyarberk.kutuphane_randevu.model.Appointment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandling {

    @ExceptionHandler({RoomNotFoundException.class})
    public ResponseEntity<Object> handleRoomNotFoundException (RoomNotFoundException ex){
        return ResponseEntity.status(404).body(ex.getMessage());
    }
    @ExceptionHandler({AppointmentNotFoundException.class})
    public ResponseEntity<String> handleAppointmenNotFoundException (AppointmentNotFoundException ex){
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}

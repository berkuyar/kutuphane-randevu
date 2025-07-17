package com.uyarberk.kutuphane_randevu.exception;

import com.uyarberk.kutuphane_randevu.model.Appointment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

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
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllOtherExceptions(Exception ex){
        return ResponseEntity.status(500).body("Beklenmeyen bir hata oluştu " + ex.getMessage());
    }
@ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<Object> handeUserNotFoundException(UserNotFoundException ex){
        return ResponseEntity.status(404).body(ex.getMessage());
}

// validasyonlar için
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    // aynı odadan varsa
    @ExceptionHandler(DuplicateRoomException.class)
    public ResponseEntity<Object> handleDuplicateRoomException(DuplicateRoomException ex){
        return ResponseEntity.status(409).body(ex.getMessage());
    }
}

package com.uyarberk.kutuphane_randevu.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.uyarberk.kutuphane_randevu.model.Room;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentUpdateRepsonseDto {

    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private String roomName;
}

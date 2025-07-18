package com.uyarberk.kutuphane_randevu.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCreateRequestDto {

    private Long userId;
    private Long roomId;
    @NotBlank(message = "Tarih boş geçilemez")
    private LocalDate date;
    @NotBlank(message = "Başlangıç saati boş geçilemez.")
    private LocalTime startTime;
    @NotBlank(message = "Bitiş saati boş geçilemez.")
    private LocalTime endTime;
}

package com.uyarberk.kutuphane_randevu.dto;

import com.uyarberk.kutuphane_randevu.model.Room;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentUpdateRequestDto {

 @NotBlank(message = "Tarih alanı boş geçilemez.")
    private LocalDate date;
 @NotBlank(message = "Başlangıç saati boş geçilemez.")
    private LocalTime startTime;
    @NotBlank(message = "Bitiş saati boş geçilemez.")
    private LocalTime endTime;

    private Long roomId;
}

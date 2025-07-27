package com.uyarberk.kutuphane_randevu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeeklyOccupancyDto {

    private Long dayOfWeek;
    private Long appointmentCount;
    private Long uniqueRooms;

}

package com.uyarberk.kutuphane_randevu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationTrendDto {
    
    private LocalDate date;
    private Long notificationCount;
    
}
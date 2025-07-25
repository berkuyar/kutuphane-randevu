package com.uyarberk.kutuphane_randevu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationDto {

    private Long id;
    private String message;
    private LocalDateTime createdAt;
    private boolean read;
}

package com.uyarberk.kutuphane_randevu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnreadNotificationStatsDto {
    
    private Long userId;
    private String userName;
    private Long unreadCount;
    
}
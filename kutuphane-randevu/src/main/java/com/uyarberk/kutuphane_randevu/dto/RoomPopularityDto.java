package com.uyarberk.kutuphane_randevu.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomPopularityDto {

    private String roomName;
    private Long appointmentCount;

}

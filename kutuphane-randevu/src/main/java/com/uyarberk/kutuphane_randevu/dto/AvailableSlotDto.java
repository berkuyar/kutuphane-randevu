package com.uyarberk.kutuphane_randevu.dto;

import java.time.LocalTime;

public class AvailableSlotDto {

    private Long slotId;
    private LocalTime startTime;
    private LocalTime endTime;
    private String name;
    private Boolean available;

    public AvailableSlotDto() {
    }

    public AvailableSlotDto(Long slotId, LocalTime startTime, LocalTime endTime, String name, Boolean available) {
        this.slotId = slotId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.available = available;
    }

    public Long getSlotId() {
        return slotId;
    }

    public void setSlotId(Long slotId) {
        this.slotId = slotId;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
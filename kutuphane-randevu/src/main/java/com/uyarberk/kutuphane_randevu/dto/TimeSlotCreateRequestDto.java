package com.uyarberk.kutuphane_randevu.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public class TimeSlotCreateRequestDto {

    @NotNull(message = "Başlangıç saati zorunludur")
    private LocalTime startTime;

    @NotNull(message = "Bitiş saati zorunludur")
    private LocalTime endTime;

    private String name;
    private String description;

    public TimeSlotCreateRequestDto() {
    }

    public TimeSlotCreateRequestDto(LocalTime startTime, LocalTime endTime, String name, String description) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
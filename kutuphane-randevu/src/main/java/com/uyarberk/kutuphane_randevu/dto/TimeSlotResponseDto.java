package com.uyarberk.kutuphane_randevu.dto;

import java.time.LocalTime;

public class TimeSlotResponseDto {

    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private String name;
    private String description;
    private Boolean isActive;

    public TimeSlotResponseDto() {
    }

    public TimeSlotResponseDto(Long id, LocalTime startTime, LocalTime endTime, String name, String description, Boolean isActive) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.description = description;
        this.isActive = isActive;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}
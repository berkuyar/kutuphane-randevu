package com.uyarberk.kutuphane_randevu.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentPeriodResponseDto {

    private Long id;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime availableStartTime;
    private LocalTime availableEndTime;
    private Boolean isActive;
    private String description;

    public AppointmentPeriodResponseDto() {
    }

    public AppointmentPeriodResponseDto(Long id, LocalDate startDate, LocalDate endDate, LocalTime availableStartTime, LocalTime availableEndTime, Boolean isActive, String description) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.availableStartTime = availableStartTime;
        this.availableEndTime = availableEndTime;
        this.isActive = isActive;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalTime getAvailableStartTime() {
        return availableStartTime;
    }

    public void setAvailableStartTime(LocalTime availableStartTime) {
        this.availableStartTime = availableStartTime;
    }

    public LocalTime getAvailableEndTime() {
        return availableEndTime;
    }

    public void setAvailableEndTime(LocalTime availableEndTime) {
        this.availableEndTime = availableEndTime;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
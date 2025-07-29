package com.uyarberk.kutuphane_randevu.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentPeriodCreateRequestDto {

    @NotNull(message = "Başlangıç tarihi zorunludur")
    private LocalDate startDate;

    @NotNull(message = "Bitiş tarihi zorunludur")
    private LocalDate endDate;

    @NotNull(message = "Müsait başlangıç saati zorunludur")
    private LocalTime availableStartTime;

    @NotNull(message = "Müsait bitiş saati zorunludur")
    private LocalTime availableEndTime;

    private String description;

    public AppointmentPeriodCreateRequestDto() {
    }

    public AppointmentPeriodCreateRequestDto(LocalDate startDate, LocalDate endDate, LocalTime availableStartTime, LocalTime availableEndTime, String description) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.availableStartTime = availableStartTime;
        this.availableEndTime = availableEndTime;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
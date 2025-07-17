package com.uyarberk.kutuphane_randevu.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class RoomCreateRequestDto {

    @NotBlank(message = "İsim boş geçilemez.")
    private String name;
    private String description;
    @Min(value = 1, message = "Kapasite minimum 1 olmalıdır.")
    private Integer capacity;

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

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public RoomCreateRequestDto(String name, String description, Integer capacity) {
        this.name = name;
        this.description = description;
        this.capacity = capacity;
    }
}

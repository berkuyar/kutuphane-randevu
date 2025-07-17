package com.uyarberk.kutuphane_randevu.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class RoomUpdateRequestDto {
    @NotBlank
    private String name;
    @Min(1)
    private Integer capacity;
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public RoomUpdateRequestDto(String name, Integer capacity, String description) {
        this.name = name;
        this.capacity = capacity;
        this.description = description;
    }
}

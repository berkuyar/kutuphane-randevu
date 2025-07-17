package com.uyarberk.kutuphane_randevu.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentResponseDto {

    private Long id;
    private Long userId;
    private String userName;

    public AppointmentResponseDto() {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    private String userEmail;

    private Long roomId;
    private String roomName;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private String Status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public AppointmentResponseDto(String userName,Long id, Long userId, String userEmail, Long roomId, String roomName, LocalDate date, LocalTime startTime, LocalTime endTime, String status) {
       this.userName = userName;
        this.id = id;
        this.userId = userId;
        this.userEmail = userEmail;
        this.roomId = roomId;
        this.roomName = roomName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        Status = status;
    }
}

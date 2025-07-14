package com.uyarberk.kutuphane_randevu.dto;

public class AppointmentResponseDto {

    private Long id;


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
    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }


    private String description;
    private Long userId;
    private String userName;
    private String userMail;
}

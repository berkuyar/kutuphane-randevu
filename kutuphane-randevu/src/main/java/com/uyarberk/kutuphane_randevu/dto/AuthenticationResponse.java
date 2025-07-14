package com.uyarberk.kutuphane_randevu.dto;



public class AuthenticationResponse {
    private String token;


    public void setToken(String token) {

        this.token = token;
    }

    public String getToken() {

        return token;
    }

    public AuthenticationResponse(String token) {

        this.token = token;
    }
}

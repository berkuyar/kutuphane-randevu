package com.uyarberk.kutuphane_randevu.dto;

public class RegisterRequest {

    public String email;
    public String password;


    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public RegisterRequest(String email, String password) {

        this.email = email;
        this.password = password;
    }
}

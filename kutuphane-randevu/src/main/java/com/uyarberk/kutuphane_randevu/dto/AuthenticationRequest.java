package com.uyarberk.kutuphane_randevu.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AuthenticationRequest {
    @NotBlank(message = "Email boş olamaz!")
    @Email(message = "Lütfen geçerli bir email giriniz.")
    private String email;
    @NotBlank(message = "Lütfen bir şifre giriniz.")
    @Size(min = 4, message = "Şifreniz en az 4 haneli olmalıdır.")
    private String password;

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

    public AuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}

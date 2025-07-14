package com.uyarberk.kutuphane_randevu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class ChangePasswordRequest {

    @NotBlank(message = "Mevcut şifre boş olamaz.")
    @NotEmpty(message = "Mevcut şifre boş olamaz.")
    private String oldPassword;
    @NotBlank(message = "Yeni şifre boş olamaz.")
    @NotEmpty(message = "Yeni şifre boş olamaz.")
    @Size(min = 4, message = "Yeni şifre en az 4 haneli olmalıdır.")
    private String newPassword;
    @NotBlank(message = "Yeni şifre boş olamaz.")
    @NotEmpty(message = "Yeni boş olamaz.")
    private String confirmNewPassword;

    public ChangePasswordRequest(String oldPassword, String newPassword, String confirmPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmNewPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmPassword) {
        this.confirmNewPassword = confirmPassword;
    }
}

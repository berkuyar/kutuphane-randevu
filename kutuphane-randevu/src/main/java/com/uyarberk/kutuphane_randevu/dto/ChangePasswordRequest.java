package com.uyarberk.kutuphane_randevu.dto;

public class ChangePasswordRequest {
    private String oldPassword;
    private String newPassword;
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

package com.uyarberk.kutuphane_randevu.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {
    @NotBlank(message = "İsim boş geçilemez.")
    private String name;
    @NotBlank(message = "Email boş geçilemez.")
    @Email(message = "Girdiğiniz email geçerli değil.")
    private String email;

}

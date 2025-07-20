package com.uyarberk.kutuphane_randevu.controller;

import com.uyarberk.kutuphane_randevu.dto.ChangePasswordRequest;
import com.uyarberk.kutuphane_randevu.dto.UserResponseDto;
import com.uyarberk.kutuphane_randevu.dto.UserUpdateRequestDto;
import com.uyarberk.kutuphane_randevu.model.User;
import com.uyarberk.kutuphane_randevu.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        log.info("Tüm kullanıcıları getirme isteği alındı.");
        List<UserResponseDto> users = userService.getAllUsers();
        log.info("Toplam {} kullanıcı bulundu.", users.size());
        return ResponseEntity.ok(users);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        log.info("Kullanıcı getirme isteği. userId={}", id);
        UserResponseDto user = userService.getUserById(id);
        log.info("Kullanıcı bulundu. userId={}, email={}", user.getId(), user.getEmail());
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        log.info("Kullanıcı silme isteği alındı. userId={}", id);
        boolean deleted = userService.deleteUserById(id);
        if (deleted) {
            log.info("Kullanıcı başarıyla silindi. userId={}", id);
            return ResponseEntity.ok("Kullanıcı başarıyla silindi");
        } else {
            log.warn("Kullanıcı silinemedi. userId={}", id);
            return ResponseEntity.status(404).body("Kullanıcı bulunamadı");
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable Long id,
                                                      @RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto) {
        log.info("Kullanıcı güncelleme isteği. userId={}", id);
        UserResponseDto user = userService.updateUser(id, userUpdateRequestDto);
        log.info("Kullanıcı başarıyla güncellendi. userId={}", id);
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordRequest request,
                                                 Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        log.info("Şifre değiştirme isteği. userId={}", user.getId());
        userService.changePassword(user.getId(), request);
        log.info("Şifre başarıyla güncellendi. userId={}", user.getId());
        return ResponseEntity.ok("Şifre başarıyla güncellendi.");
    }
}

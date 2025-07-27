package com.uyarberk.kutuphane_randevu.service;

// Kullanıcı modelini ve repository'yi içeri aktarıyoruz
import com.uyarberk.kutuphane_randevu.dto.ChangePasswordRequest;
import com.uyarberk.kutuphane_randevu.dto.UserResponseDto;
import com.uyarberk.kutuphane_randevu.dto.UserUpdateRequestDto;
import com.uyarberk.kutuphane_randevu.exception.UserNotFoundException;
import com.uyarberk.kutuphane_randevu.model.User;
import com.uyarberk.kutuphane_randevu.repository.UserRepository;

// Spring'in servis anotasyonu ve bağımlılık enjeksiyonu için gerekli sınıflar
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service // Bu sınıf bir "servis katmanı" olduğunu belirtir (iş mantığı burada yazılır)
public class UserService {

    @Autowired // Spring, bu interface'i otomatik olarak bağlar (dependency injection)
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserResponseDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(user -> new UserResponseDto(user.getId  (), user.getName(), user.getEmail()))
                .collect(Collectors.toList());
    }

    public UserResponseDto getUserById(Long id) {
         User user = userRepository.findById(id).orElseThrow(() -> {
             log.error("Kullanıcı bulunamadı. userId={}", id );
             return new UserNotFoundException("Kullanıcı bulunamadı.");
        });
         return new  UserResponseDto(user.getId(), user.getName(), user.getEmail());

    }
    // UserService.java
    public boolean deleteUserById(Long id) {
        log.info("Kullanıcı silme isteği alındı. userId={}", id );
            User user = userRepository.findById(id).orElseThrow(() -> {
                log.error("Kullanıcı bulunamadı. userId={}", id);
                return new UserNotFoundException("Kullanıcı bulunamadı.");
            });
            userRepository.deleteById(id);
            log.info("Kullanıcı başarıyla silindi");
            return true;
        }

    public UserResponseDto updateUser(Long id, UserUpdateRequestDto updatedUser) {
        log.info("Kullanıcı güncelleme isteği alındı. userId={}", id );

        User user = userRepository.findById(id).orElseThrow(() -> {
             log.error("Kullanıcı bulunamadı. userId={}", id);
             return new UserNotFoundException("Kullanıcı bulunamadı.");
        });
         if(userRepository.existsByEmail(updatedUser.getEmail()) && !user.getEmail().equals(updatedUser.getEmail())){
             log.warn("E-posta zaten kullanımda. email={}", updatedUser.getEmail());
             throw new RuntimeException("Bu e-posta başka bir kullanıcı tarafından kullanılıyor.");
        }
          user.setName(updatedUser.getName());
          user.setEmail(updatedUser.getEmail());

          userRepository.save(user);
          log.info("Kullanıcı başarıyla kaydedildi. userId={}", id );

          UserResponseDto userResponseDto = new UserResponseDto(user.getId(), user.getName(), user.getEmail());

          return userResponseDto;
        }



    public void  changePassword(Long userId, ChangePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Eski şifre yanlış");
        }

        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new RuntimeException("Yeni şifreler eşleşmiyor");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        log.info("Şifre başarıyla değiştirildi. ");
    }
    }




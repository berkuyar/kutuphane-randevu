package com.uyarberk.kutuphane_randevu.controller;

import com.uyarberk.kutuphane_randevu.dto.ChangePasswordRequest;
import com.uyarberk.kutuphane_randevu.model.User;
import com.uyarberk.kutuphane_randevu.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController // Bu sınıf HTTP isteklerini karşılar
@RequestMapping("/api/users") // /api/users ile başlayan tüm istekleri yakalar
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Kullanıcı kayıt işlemi (POST /api/users/register)
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        try {
            User savedUser = userService.register(user); // Kullanıcıyı kaydet
            return ResponseEntity.ok(savedUser); // Başarılıysa 200 OK + kullanıcı
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build(); // Hata varsa 400 Bad Request
        }
    }

    // Kullanıcı giriş işlemi (POST /api/users/login)
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        Optional<User> result = userService.login(user.getEmail(), user.getPassword());

        if (result.isPresent()) {
            return ResponseEntity.ok(result.get()); // Giriş başarılıysa kullanıcıyı döner
        } else {
            return ResponseEntity.status(401).build(); // Giriş hatalıysa 401 döner
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllUsers(){

        return ResponseEntity.ok(userService.getAllUsers());
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
       Optional<User> user = userService.getUserById(id);

       if(user.isPresent()){
           return ResponseEntity.ok(user.get());
       }else {
           return ResponseEntity.notFound().build();
       }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        boolean deleted = userService.deleteUserById(id);

        if(deleted){
            return ResponseEntity.ok("Kullanıcı başarıyla silindi.");
        }else{
            return ResponseEntity.status(404).body("Kullanıcı bulunamadı.");
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/id")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser){

        Optional<User> user = userService.updateUser(id, updatedUser);

        if(user.isPresent()){
            return ResponseEntity.ok(user.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal(); // JWT token'dan gelen kullanıcı
        try {
            userService.changePassword(user.getId(), request);
            return ResponseEntity.ok("Şifre başarıyla güncellendi.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }



}
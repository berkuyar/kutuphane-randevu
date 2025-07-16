package com.uyarberk.kutuphane_randevu.service;

// Kullanıcı modelini ve repository'yi içeri aktarıyoruz
import com.uyarberk.kutuphane_randevu.dto.ChangePasswordRequest;
import com.uyarberk.kutuphane_randevu.exception.UserNotFoundException;
import com.uyarberk.kutuphane_randevu.model.User;
import com.uyarberk.kutuphane_randevu.repository.UserRepository;

// Spring'in servis anotasyonu ve bağımlılık enjeksiyonu için gerekli sınıflar
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service // Bu sınıf bir "servis katmanı" olduğunu belirtir (iş mantığı burada yazılır)
public class UserService {

    @Autowired // Spring, bu interface'i otomatik olarak bağlar (dependency injection)
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Kullanıcı kayıt işlemi
     * 1. Email zaten kayıtlı mı kontrol edilir
     * 2. Şifre düz metin olarak kaydedilir (ileride hash yapılacak)
     * 3. Kullanıcı USER rolüyle kayıt edilir
     */
    public User register(User user) {
        // Aynı email varsa kayıt işlemini engelliyoruz
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());

        // Eğer kullanıcı zaten varsa hata fırlat
        if (existingUser.isPresent()) {
            throw new RuntimeException("Bu e-posta zaten kayıtlı!");
        }

        // Şifreyi hash'lemek için buraya ileride kod eklenecek
        // Şimdilik düz olarak kaydediyoruz
        // user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Varsayılan olarak tüm yeni kullanıcılar "USER" rolüyle oluşturulur
        user.setRole(User.Role.USER);

        // Kullanıcıyı veritabanına kaydet ve geri döndür
        return userRepository.save(user);
    }

    /**
     * Giriş işlemi
     * 1. Email ile kullanıcı veritabanında aranır
     * 2. Şifre eşleşiyorsa kullanıcı döndürülür, eşleşmiyorsa boş Optional
     */
    public Optional<User> login(String email, String password) {
        // Email'e göre kullanıcıyı veritabanından bul
        Optional<User> user = userRepository.findByEmail(email);

        // Eğer kullanıcı varsa ve şifre doğruysa döndür
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }

        // Giriş başarısızsa boş Optional döndür (kullanıcı yok ya da şifre yanlış)
        return Optional.empty();
    }
    /**
     * Veritabanındaki tüm kullanıcıları getirir
     */
    public List<User> getAllUsers() {
        return userRepository.findAll(); // Spring Data JPA otomatik olarak bu fonksiyonu sağlar
    }
    /**
     * ID’ye göre kullanıcıyı getirir
     */
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("Kullanıcı bulunamadı"));
    }
    // UserService.java
    public boolean deleteUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            userRepository.deleteById(id);
            return true; // başarıyla silindi
        }
        return false; // kullanıcı bulunamadı
    }

    public Optional<User> updateUser(Long id, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()) {
            User user = existingUser.get();

            // Alanları güncelle
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword()); // düz olarak güncelle (şimdilik)
            user.setRole(updatedUser.getRole());

            userRepository.save(user); // veritabanına kaydet
            return Optional.of(user);
        }

        return Optional.empty(); // kullanıcı bulunamazsa
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
    }
    }




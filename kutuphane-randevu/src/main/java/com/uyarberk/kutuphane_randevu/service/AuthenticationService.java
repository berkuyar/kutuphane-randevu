package com.uyarberk.kutuphane_randevu.service;

import com.uyarberk.kutuphane_randevu.dto.AuthenticationRequest;
import com.uyarberk.kutuphane_randevu.dto.AuthenticationResponse;
import com.uyarberk.kutuphane_randevu.dto.RegisterRequest;
import com.uyarberk.kutuphane_randevu.model.User;
import com.uyarberk.kutuphane_randevu.repository.UserRepository;
import com.uyarberk.kutuphane_randevu.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
// Otomatik constructor injection (final alanlar için)
public class AuthenticationService {

    private final UserRepository userRepository; //kullanıcıyı veritabanından email ile bulmamıza yarar
    private final PasswordEncoder passwordEncoder; // Kullanıcının şifresini hash’leyeceğiz ya? Bu sınıf BCrypt ile şifreyi kodlamaya veya doğrulamaya yarar.
    private final JwtService jwtService; //→ Token üretme ve çözme işlemlerini yapar.
    private final AuthenticationManager authenticationManager;  //Spring Security’nin giriş kontrol sistemidir.
    //Bu şifre doğru mu, bu kullanıcı kayıtlı mı?" gibi işleri yapar.
    //Kullanıcıyı biz bulmuyoruz, bu yapı bulur.

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtservice) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtservice;
    }

    // Login işlemi burada yapılır
    public AuthenticationResponse login(AuthenticationRequest request) {  //Kullanıcı bize login olmak için email + password gönderiyor.
        //Bu metot: şifreyi kontrol eder, doğruysa token üretir.
        // 1. Kullanıcının email ve şifresini doğrula

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // 2. Kullanıcıyı DB’den bul (email ile)
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        // 3. Token oluştur
        String jwtToken = jwtService.generateToken(user);

        // 4. Token'ı response ile döndür
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse register(RegisterRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(User.Role.USER);

        userRepository.save(user);

        String jwt = jwtService.generateToken(user);
        return new AuthenticationResponse(jwt);
    }

}

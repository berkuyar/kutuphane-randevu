package com.uyarberk.kutuphane_randevu.controller;

import com.uyarberk.kutuphane_randevu.dto.AuthenticationRequest;
import com.uyarberk.kutuphane_randevu.dto.AuthenticationResponse;
import com.uyarberk.kutuphane_randevu.dto.RegisterRequest;
import com.uyarberk.kutuphane_randevu.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;

    }
    // kayıt endpointi
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegisterRequest request){
        AuthenticationResponse response = authenticationService.register(request);

        return ResponseEntity.ok(response);
    }

    // giriş endpointi
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login (@RequestBody AuthenticationRequest request){
        AuthenticationResponse response = authenticationService.login(request);

        return ResponseEntity.ok(response);
    }
}

package com.uyarberk.kutuphane_randevu.security;

import com.uyarberk.kutuphane_randevu.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // 🔐 Gizli imzalama anahtarı (gerçek projede env'den alınır)
    private static final String SECRET_KEY = "mysecretkeymysecretkeymysecretkeymysecretkey";

    // 🛠 1. Token üretme (giriş başarılıysa)
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name()); // İstersen role, id gibi şeyler de ekleyebilirsin
        return Jwts.builder()
                .setClaims(claims) // Payload'a ek veri
                .setSubject(user.getEmail()) // Kimlik bilgisi (email gibi)
                .setIssuedAt(new Date(System.currentTimeMillis())) // ne zaman oluşturuldu
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 saat geçerli
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // imzalama
                .compact();
    }

    // 🧪 2. Token geçerli mi? (doğrulama işlemi)
    public boolean isTokenValid(String token, User user) {
        final String username = extractUsername(token);
        return (username.equals(user.getEmail())) && !isTokenExpired(token);
    }

    // 📤 3. Token'dan email’i çıkar (subject = email)
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 📦 Token’dan bir "claim" (veri) çıkar
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // ⌛ Süresi dolmuş mu?
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 🕒 Token'daki expire tarihi
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 🔍 Token içindeki tüm veriyi çöz
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey()) // Doğrulamak için anahtarı ayarla
                .build()
                .parseClaimsJws(token) // Token'ı çöz
                .getBody(); // İçindeki payload (veriler)
    }

    // 🔐 Anahtarı elde et (SECRET_KEY'den gerçek imza anahtısı üret)
    private Key getSignInKey() {
        byte[] keyBytes = SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

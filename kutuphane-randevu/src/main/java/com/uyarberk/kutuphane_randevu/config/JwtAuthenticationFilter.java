package com.uyarberk.kutuphane_randevu.config;

import com.uyarberk.kutuphane_randevu.repository.UserRepository;
import com.uyarberk.kutuphane_randevu.security.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    /**
     * Bu metod sayesinde /api/auth/** ile başlayan endpoint'ler
     * (örneğin login ve register) JWT filtresine takılmaz, doğrudan geçer.
     * Böylece login olmaya çalışan birinden token istenmez.
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Authorization header'ını al
        final String authHeader = request.getHeader("Authorization");

        // 2. Header yoksa ya da "Bearer " ile başlamıyorsa → filtreleme yapmadan devam et
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. "Bearer ..." kısmından sonra gelen token'ı çıkar
        final String jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwt); // token içinden email'i al

        // 4. Eğer email varsa ve SecurityContext zaten dolu değilse (yani giriş yapılmamışsa)
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 5. Veritabanından kullanıcıyı al
            var user = userRepository.findByEmail(userEmail)
                    .orElseThrow();

            // 6. Token geçerli mi kontrol et
            if (jwtService.isTokenValid(jwt, user)) {

                // 7. Kullanıcıyı sisteme tanıt (SecurityContext'e yerleştir)
                var authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        user.getAuthorities() // Rol gibi yetkiler
                );

                // 8. Ek detayları ekle (IP adresi, session vs.)
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // 9. Authentication nesnesini sisteme tanıt
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 10. Filtre zincirine devam et
        filterChain.doFilter(request, response);
    }
}

package com.uyarberk.kutuphane_randevu.controller;

import com.uyarberk.kutuphane_randevu.dto.AdminDashboardDto;
import com.uyarberk.kutuphane_randevu.service.AdminDashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    public AdminDashboardController(AdminDashboardService adminDashboardService) {
        this.adminDashboardService = adminDashboardService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public ResponseEntity<AdminDashboardDto> getDashboardData() {
        log.info("Admin dashboard verisi isteniyor...");
        AdminDashboardDto dto = adminDashboardService.getDashboardData();
        log.info("Admin dashboard verisi başarıyla oluşturuldu.");
        return ResponseEntity.ok(dto);
    }
}

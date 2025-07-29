package com.uyarberk.kutuphane_randevu.controller;

import com.uyarberk.kutuphane_randevu.dto.AppointmentPeriodCreateRequestDto;
import com.uyarberk.kutuphane_randevu.dto.AppointmentPeriodResponseDto;
import com.uyarberk.kutuphane_randevu.service.AppointmentPeriodService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/appointment-periods")
public class AppointmentPeriodController {

    private final AppointmentPeriodService appointmentPeriodService;

    public AppointmentPeriodController(AppointmentPeriodService appointmentPeriodService) {
        this.appointmentPeriodService = appointmentPeriodService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AppointmentPeriodResponseDto> createPeriod(@RequestBody @Valid AppointmentPeriodCreateRequestDto dto) {
        log.info("Yeni randevu dönemi oluşturma isteği alındı");
        AppointmentPeriodResponseDto response = appointmentPeriodService.createPeriod(dto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AppointmentPeriodResponseDto>> getAllActivePeriods() {
        log.info("Tüm aktif randevu dönemleri getirme isteği alındı");
        List<AppointmentPeriodResponseDto> periods = appointmentPeriodService.getAllActivePeriods();
        return ResponseEntity.ok(periods);
    }

    @GetMapping("/current")
    public ResponseEntity<List<AppointmentPeriodResponseDto>> getCurrentAndFuturePeriods() {
        log.info("Güncel ve gelecek randevu dönemleri getirme isteği alındı");
        List<AppointmentPeriodResponseDto> periods = appointmentPeriodService.getCurrentAndFuturePeriods();
        return ResponseEntity.ok(periods);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentPeriodResponseDto> getPeriodById(@PathVariable Long id) {
        log.info("Randevu dönemi getirme isteği alındı: id={}", id);
        AppointmentPeriodResponseDto period = appointmentPeriodService.getPeriodById(id);
        return ResponseEntity.ok(period);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePeriod(@PathVariable Long id) {
        log.info("Randevu dönemi silme isteği alındı: id={}", id);
        appointmentPeriodService.deletePeriod(id);
        return ResponseEntity.ok("Randevu dönemi başarıyla deaktif edildi.");
    }
}
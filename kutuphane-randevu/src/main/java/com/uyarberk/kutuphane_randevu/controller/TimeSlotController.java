package com.uyarberk.kutuphane_randevu.controller;

import com.uyarberk.kutuphane_randevu.dto.TimeSlotCreateRequestDto;
import com.uyarberk.kutuphane_randevu.dto.TimeSlotResponseDto;
import com.uyarberk.kutuphane_randevu.dto.AvailableSlotDto;
import com.uyarberk.kutuphane_randevu.service.TimeSlotService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/time-slots")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;

    public TimeSlotController(TimeSlotService timeSlotService) {
        this.timeSlotService = timeSlotService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TimeSlotResponseDto> createSlot(@RequestBody @Valid TimeSlotCreateRequestDto dto) {
        log.info("Yeni zaman slotu oluşturma isteği alındı");
        TimeSlotResponseDto response = timeSlotService.createSlot(dto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<TimeSlotResponseDto>> getAllActiveSlots() {
        log.info("Tüm aktif zaman slotları getirme isteği alındı");
        List<TimeSlotResponseDto> slots = timeSlotService.getAllActiveSlots();
        return ResponseEntity.ok(slots);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<TimeSlotResponseDto> getSlotById(@PathVariable Long id) {
        log.info("Zaman slotu getirme isteği alındı: id={}", id);
        TimeSlotResponseDto slot = timeSlotService.getSlotById(id);
        return ResponseEntity.ok(slot);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSlot(@PathVariable Long id) {
        log.info("Zaman slotu silme isteği alındı: id={}", id);
        timeSlotService.deleteSlot(id);
        return ResponseEntity.ok("Zaman slotu başarıyla deaktif edildi.");
    }

    @GetMapping("/available")
    public ResponseEntity<List<AvailableSlotDto>> getAvailableSlots(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("roomId") Long roomId) {
        log.info("Müsait slotlar sorgusu: date={}, roomId={}", date, roomId);
        List<AvailableSlotDto> availableSlots = timeSlotService.getAvailableSlotsForDate(date, roomId);
        return ResponseEntity.ok(availableSlots);
    }
}
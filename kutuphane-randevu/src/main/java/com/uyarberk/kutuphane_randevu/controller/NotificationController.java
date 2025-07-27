package com.uyarberk.kutuphane_randevu.controller;

import com.uyarberk.kutuphane_randevu.dto.NotificationDto;
import com.uyarberk.kutuphane_randevu.dto.NotificationTrendDto;
import com.uyarberk.kutuphane_randevu.dto.UnreadNotificationStatsDto;
import com.uyarberk.kutuphane_randevu.model.User;
import com.uyarberk.kutuphane_randevu.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    //Kullanıcının tüm bildirimleribi getir.
    @GetMapping
    public ResponseEntity<List<NotificationDto>> getMyNotifications(
            @AuthenticationPrincipal User user // doğrudan entity geliyor
    ) {
        Long userId = user.getId();
        List<NotificationDto> notifications = notificationService.getNotificationsForUser(userId);
        return ResponseEntity.ok(notifications);
    }

    // Kullanıcının sadece okunmamış bildirimlerini getir
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDto>> getMyUnreadNotifications(
            @AuthenticationPrincipal User user
    ) {
        Long userId = user.getId();
        List<NotificationDto> notifications = notificationService.getUnreadNotificationsForUser(userId);
        return ResponseEntity.ok(notifications);
    }


    @PutMapping("/read/{id}")
    public ResponseEntity<String> readNotifications(@PathVariable Long id, @AuthenticationPrincipal User user) {
        notificationService.markAsRead(id, user.getId());
        return ResponseEntity.ok("Bildirim okundu olarak işaretlendi");
    }

    // Analitik endpoint'ler - Sadece Admin
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/unread")
    public ResponseEntity<List<UnreadNotificationStatsDto>> getUnreadNotificationStats() {
        List<UnreadNotificationStatsDto> stats = notificationService.getUnreadNotificationStats();
        return ResponseEntity.ok(stats);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/trend")
    public ResponseEntity<List<NotificationTrendDto>> getNotificationTrend(
            @RequestParam(defaultValue = "30") int days
    ) {
        List<NotificationTrendDto> trend = notificationService.getDailyNotificationTrend(days);
        return ResponseEntity.ok(trend);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stats/total-unread")
    public ResponseEntity<Long> getTotalUnreadCount() {
        Long count = notificationService.getTotalUnreadCount();
        return ResponseEntity.ok(count);
    }

    // Kullanıcı kendi okunmamış bildirim sayısını görebilir
    @GetMapping("/my/unread-count")
    public ResponseEntity<Long> getMyUnreadCount(@AuthenticationPrincipal User user) {
        Long count = notificationService.getUnreadCountForUser(user.getId());
        return ResponseEntity.ok(count);
    }

}


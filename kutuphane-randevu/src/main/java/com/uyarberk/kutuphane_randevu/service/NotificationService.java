package com.uyarberk.kutuphane_randevu.service;

import com.uyarberk.kutuphane_randevu.dto.NotificationDto;
import com.uyarberk.kutuphane_randevu.dto.NotificationTrendDto;
import com.uyarberk.kutuphane_randevu.dto.UnreadNotificationStatsDto;
import com.uyarberk.kutuphane_randevu.exception.NotificationNotFoundException;
import com.uyarberk.kutuphane_randevu.exception.UnauthorizeNotFoundException;
import com.uyarberk.kutuphane_randevu.model.Notification;
import com.uyarberk.kutuphane_randevu.model.User;
import com.uyarberk.kutuphane_randevu.repository.NotificationRepository;
import com.uyarberk.kutuphane_randevu.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor //final alan için otomatik constructor oluşturur
@Builder
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final WebSocketNotificationService webSocketNotificationService;
    private final UserRepository userRepository;

    public void createNotification(String message, Long userId) {
        Notification notification = Notification.builder() //builder ile notification nesnesi oluşturuyoruz
                .message(message)
                .userId(userId)
                .createdAt(LocalDateTime.now())
                .read(false)
                .build();

        // Veritabanına kaydet
        Notification savedNotification = notificationRepository.save(notification);
        
        // DTO'ya çevir
        NotificationDto notificationDto = new NotificationDto();
        notificationDto.setId(savedNotification.getId());
        notificationDto.setMessage(savedNotification.getMessage());
        notificationDto.setCreatedAt(savedNotification.getCreatedAt());
        notificationDto.setRead(savedNotification.isRead());
        
        // 🔥 WebSocket ile gerçek zamanlı gönder!
        webSocketNotificationService.sendNotificationToUser(userId, notificationDto);
        
        log.info("Bildirim oluşturuldu ve WebSocket ile gönderildi: userId={}, message={}", userId, message);
    }

    public List<NotificationDto> getNotificationsForUser(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserId(userId);
        return notifications.stream().map(notification -> {
            NotificationDto dto = new NotificationDto();
            dto.setId(notification.getId());
            dto.setMessage(notification.getMessage());
            dto.setCreatedAt(notification.getCreatedAt());
            dto.setRead(notification.isRead());
            return dto;
        }).toList();
    }

    public List<NotificationDto> getUnreadNotificationsForUser(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndReadFalse(userId);
        return notifications.stream().map(notification -> {
            NotificationDto dto = new NotificationDto();
            dto.setId(notification.getId());
            dto.setMessage(notification.getMessage());
            dto.setCreatedAt(notification.getCreatedAt());
            dto.setRead(notification.isRead());
            return dto;
        }).toList();
    }

    public void markAsRead(Long id, Long userId) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException("Bildirim bulunamadı."));

        if (!notification.getUserId().equals(userId)) {
            throw new UnauthorizeNotFoundException("Bu bildirime erişim yetkiniz yok.");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    // Analitik metodlar
    public List<UnreadNotificationStatsDto> getUnreadNotificationStats() {
        log.info("Okunmamış bildirim istatistikleri sorgusu");
        return notificationRepository.findUnreadNotificationStats();
    }

    public List<NotificationTrendDto> getDailyNotificationTrend(int days) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(days);
        log.info("Son {} günün bildirim trendi sorgusu", days);
        return notificationRepository.findDailyNotificationTrend(startDate);
    }

    public Long getUnreadCountForUser(Long userId) {
        return notificationRepository.countUnreadByUserId(userId);
    }

    public Long getTotalUnreadCount() {
        return notificationRepository.countTotalUnread();
    }

    /**
     * Admin tarafından tüm kullanıcılara duyuru gönderir
     * @param message Duyuru mesajı
     */
    public void sendAnnouncementToAllUsers(String message) {
        log.info("Tüm kullanıcılara duyuru gönderiliyor: {}", message);
        
        // Tüm kullanıcıları getir
        List<User> allUsers = userRepository.findAll();
        
        // Her kullanıcıya bildirim oluştur ve veritabanına kaydet
        for (User user : allUsers) {
            createNotification(message, user.getId());
        }
        
        // WebSocket ile genel duyuru gönder
        webSocketNotificationService.sendGlobalAnnouncement(message);
        
        log.info("Duyuru {} kullanıcıya gönderildi", allUsers.size());
    }

}
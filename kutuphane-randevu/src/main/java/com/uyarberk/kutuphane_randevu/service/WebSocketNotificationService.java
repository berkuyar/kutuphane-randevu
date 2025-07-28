package com.uyarberk.kutuphane_randevu.service;

import com.uyarberk.kutuphane_randevu.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebSocketNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Belirli bir kullanıcıya gerçek zamanlı bildirim gönderir
     * @param userId Bildirim gönderilecek kullanıcı ID'si
     * @param notification Gönderilecek bildirim
     */
    public void sendNotificationToUser(Long userId, NotificationDto notification) {
        try {
            // Kullanıcıya özel kanal: /queue/notifications/{userId}
            String destination = "/queue/notifications/" + userId;
            
            log.info("WebSocket bildirimi gönderiliyor: userId={}, destination={}", userId, destination);
            
            messagingTemplate.convertAndSend(destination, notification);
            
            log.info("WebSocket bildirimi başarıyla gönderildi: userId={}", userId);
            
        } catch (Exception e) {
            log.error("WebSocket bildirimi gönderilirken hata: userId={}, error={}", userId, e.getMessage());
        }
    }

    /**
     * Tüm kullanıcılara genel duyuru gönderir (admin tarafından)
     * @param announcement Duyuru mesajı
     */
    public void sendGlobalAnnouncement(String announcement) {
        try {
            log.info("Global duyuru gönderiliyor: {}", announcement);
            
            messagingTemplate.convertAndSend("/topic/announcements", announcement);
            
            log.info("Global duyuru başarıyla gönderildi");
            
        } catch (Exception e) {
            log.error("Global duyuru gönderilirken hata: error={}", e.getMessage());
        }
    }

}
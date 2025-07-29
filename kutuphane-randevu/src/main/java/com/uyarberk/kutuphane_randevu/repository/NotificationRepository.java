package com.uyarberk.kutuphane_randevu.repository;

import com.uyarberk.kutuphane_randevu.dto.NotificationTrendDto;
import com.uyarberk.kutuphane_randevu.dto.UnreadNotificationStatsDto;
import com.uyarberk.kutuphane_randevu.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserId(Long userId);
    
    // Kullanıcının sadece okunmamış bildirimlerini getir
    List<Notification> findByUserIdAndIsReadFalse(Long userId);

    // Kullanıcıların okunmamış bildirim istatistikleri
    @Query("SELECT new com.uyarberk.kutuphane_randevu.dto.UnreadNotificationStatsDto(" +
           "n.userId, " +
           "(SELECT u.name FROM User u WHERE u.id = n.userId), " +
           "COUNT(n)) " +
           "FROM Notification n " +
           "WHERE n.isRead = false " +
           "GROUP BY n.userId " +
           "ORDER BY COUNT(n) DESC")
    List<UnreadNotificationStatsDto> findUnreadNotificationStats();

    // Günlük bildirim trendi - son X gün :// her gün kaç bildirim geldiğini gösterir
    @Query("SELECT new com.uyarberk.kutuphane_randevu.dto.NotificationTrendDto(" +
           "FUNCTION('DATE', n.createdAt), " +
           "COUNT(n)) " +
           "FROM Notification n " +
           "WHERE n.createdAt >= :startDate " +
           "GROUP BY FUNCTION('DATE', n.createdAt) " +
           "ORDER BY FUNCTION('DATE', n.createdAt)")
    List<NotificationTrendDto> findDailyNotificationTrend(@Param("startDate") LocalDateTime startDate);

    // Kullanıcının okunmamış bildirim sayısı
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.userId = :userId AND n.isRead = false")
    Long countUnreadByUserId(@Param("userId") Long userId);

    // Tüm kullanıcıların toplam okunmamış bildirimi
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.isRead = false")
    Long countTotalUnread();
}

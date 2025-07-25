package com.uyarberk.kutuphane_randevu.repository;

import com.uyarberk.kutuphane_randevu.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByUserId(Long userId);
}

package com.uyarberk.kutuphane_randevu.service;

import com.uyarberk.kutuphane_randevu.dto.AdminDashboardDto;
import com.uyarberk.kutuphane_randevu.model.Appointment;
import com.uyarberk.kutuphane_randevu.repository.AppointmentRepository;
import com.uyarberk.kutuphane_randevu.repository.RoomRepository;
import com.uyarberk.kutuphane_randevu.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final RoomRepository roomRepository;

    public AdminDashboardService(UserRepository userRepository, AppointmentRepository appointmentRepository, RoomRepository roomRepository) {
        this.userRepository = userRepository;
        this.appointmentRepository = appointmentRepository;
        this.roomRepository = roomRepository;
    }

    public AdminDashboardDto getDashboardData() {
        log.info("Admin dashboard verisi hazırlanıyor...");

        AdminDashboardDto dto = new AdminDashboardDto();

        long totalUsers = userRepository.count();
        long totalAppointments = appointmentRepository.count();
        long todayAppointments = appointmentRepository.countByDate(LocalDate.now());
        long availableRooms = getAvailableRoomCount();
        String mostPopularHour = getMostPopularHour();

        log.info("Toplam kullanıcı sayısı: {}", totalUsers);
        log.info("Toplam randevu sayısı: {}", totalAppointments);
        log.info("Bugünkü randevu sayısı: {}", todayAppointments);
        log.info("Boş oda sayısı (bugün): {}", availableRooms);
        log.info("En popüler saat: {}", mostPopularHour);

        dto.setTotalUsers(totalUsers);
        dto.setTotalAppointments(totalAppointments);
        dto.setTodayAppointments(todayAppointments);
        dto.setAvailableRooms(availableRooms);
        dto.setMostPopularHour(mostPopularHour);

        return dto;
    }

    private long getAvailableRoomCount() {
        LocalDate today = LocalDate.now();
        log.info("Boş odalar hesaplanıyor. Tarih: {}", today);

        Set<Long> busyRoomIds = appointmentRepository.findByDate(today).stream()
                .map(app -> app.getRoom().getId())
                .collect(Collectors.toSet());

        long availableCount = roomRepository.findAll().stream()
                .filter(room -> !busyRoomIds.contains(room.getId()))
                .count();

        log.info("Bugün müsait oda sayısı: {}", availableCount);
        return availableCount;
    }

    private String getMostPopularHour() {
        log.info("En popüler saat hesaplanıyor...");

        List<Appointment> allAppointments = appointmentRepository.findAll();

        Map<Integer, Long> hourCountMap = allAppointments.stream()
                .collect(Collectors.groupingBy(
                        app -> app.getStartTime().getHour(),
                        Collectors.counting()
                ));

        return hourCountMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> {
                    String hourStr = String.format("%02d:00", entry.getKey());
                    log.info("En yoğun saat bulundu: {} ({} randevu)", hourStr, entry.getValue());
                    return hourStr;
                })
                .orElseGet(() -> {
                    log.warn("En popüler saat bulunamadı. Hiç randevu yok.");
                    return "Veri yok";
                });
    }
}

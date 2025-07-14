package com.uyarberk.kutuphane_randevu.service;

import com.uyarberk.kutuphane_randevu.dto.AdminDashboardDto;
import com.uyarberk.kutuphane_randevu.model.Appointment;
import com.uyarberk.kutuphane_randevu.repository.AppointmentRepository;
import com.uyarberk.kutuphane_randevu.repository.RoomRepository;
import com.uyarberk.kutuphane_randevu.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
    public AdminDashboardDto getDashboardData(){
        AdminDashboardDto dto = new AdminDashboardDto();

        dto.setTotalUsers(userRepository.count());
        dto.setTotalAppointments(appointmentRepository.count());
        dto.setTodayAppointments(appointmentRepository.countByDate(LocalDate.now()));
        dto.setAvailableRooms(getAvailableRoomCount());
        dto.setMostPopularHour(getMostPopularHour());
        return dto;
    }
    private long getAvailableRoomCount(){
        var today = LocalDate.now();

        Set<Long> busyRoomIds = appointmentRepository.findByDate(today).stream()
                .map(app -> app.getRoom().getId())
                .collect(Collectors.toSet());

        // Boş odalar = tüm odalar - dolu odalar
        return roomRepository.findAll().stream()
                .filter(room -> !busyRoomIds.contains(room.getId()))
                .count();
    }

    private String getMostPopularHour() {
        List<Appointment> allAppointments = appointmentRepository.findAll();

        Map<Integer, Long> hourCountMap = allAppointments.stream()
                .collect(Collectors.groupingBy(
                        app -> app.getStartTime().getHour(),
                        Collectors.counting()
                ));

        return hourCountMap.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(entry -> String.format("%02d:00", entry.getKey()))
                .orElse("Veri yok");
}
}
// com.uyarberk.kutuphane_randevu.repository.AppointmentRepository
package com.uyarberk.kutuphane_randevu.repository;

import com.uyarberk.kutuphane_randevu.model.Appointment;
import com.uyarberk.kutuphane_randevu.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Belirli bir odada, belirli tarihte, verilen zaman aralığında çakışan randevuları getir
    List<Appointment> findByRoomAndDateAndStartTimeLessThanAndEndTimeGreaterThan(
            Room room, LocalDate date, LocalTime endTime, LocalTime startTime
    );
    List<Appointment> findByUserId(Long userId);

}

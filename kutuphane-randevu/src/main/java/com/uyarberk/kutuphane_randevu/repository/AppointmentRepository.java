// com.uyarberk.kutuphane_randevu.repository.AppointmentRepository
package com.uyarberk.kutuphane_randevu.repository;

import com.uyarberk.kutuphane_randevu.model.Appointment;
import com.uyarberk.kutuphane_randevu.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


    /**
     * Belirli bir tarihte, belirli saat aralığında ve isteğe bağlı olarak belirli bir odadaki randevuları getirir.
     * Eğer room parametresi null ise tüm odalardaki randevular getirilir.
     *
     * @param date      Randevu tarihi (zorunlu)
     * @param startTime Başlangıç saati (dahil)
     * @param endTime   Bitiş saati (dahil)
     * @param room      Oda (isteğe bağlı). Null verilirse tüm odalar aranır.
     * @return          Filtreye uyan randevu listesi
     */
    @Query("SELECT a FROM Appointment a " +
            "WHERE a.date = :date " +                          // Tarihi eşleştir
            "AND a.startTime >= :startTime " +                // Başlangıç saatinden sonra olanları al
            "AND a.endTime <= :endTime " +                    // Bitiş saatinden önce olanları al
            "AND (:room IS NULL OR a.room = :room)")          // Oda verilmişse eşleşeni al, verilmemişse tüm odalar
    List<Appointment> findAppointmentsByDateAndTimeRange(
            @Param("date") LocalDate date,                   // Tarih parametresi
            @Param("startTime") LocalTime startTime,         // Başlangıç saati
            @Param("endTime") LocalTime endTime,             // Bitiş saati
            @Param("room") Room room                         // Oda nesnesi (nullable)
    );

}

package com.uyarberk.kutuphane_randevu.service;

// Gerekli model ve repository sınıfı içe aktarılır
import com.uyarberk.kutuphane_randevu.dto.AppointmentResponseDto;
import com.uyarberk.kutuphane_randevu.exception.AppointmentNotFoundException;
import com.uyarberk.kutuphane_randevu.model.Appointment;
import com.uyarberk.kutuphane_randevu.model.Room;
import com.uyarberk.kutuphane_randevu.repository.AppointmentRepository;

import com.uyarberk.kutuphane_randevu.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service // Bu sınıf bir servis bileşenidir, Spring tarafından yönetilir
public class AppointmentService {

    // Appointment işlemleri için repository'e ihtiyacımız var
    private final AppointmentRepository appointmentRepository;
    private final RoomRepository roomRepository;

    // Constructor üzerinden repository enjekte edilir
    public AppointmentService(AppointmentRepository appointmentRepository, RoomRepository roomRepository) {
        this.appointmentRepository = appointmentRepository;
        this.roomRepository = roomRepository;
    }

    /**
     * Tüm randevuları getirir
     * @return List<Appointment>
     */
    public List<Appointment> getAllAppointments() {
        // Repository'den tüm kayıtları alır
        return appointmentRepository.findAll();
    }

    /**
     * Belirli ID'ye göre randevuyu getirir
     * @param id Randevunun ID'si
     * @return Optional<Appointment> – olabilir de olmayabilir de
     */
    public AppointmentResponseDto getAppointmentById(Long id) {
        Appointment a = appointmentRepository.findById(id).orElseThrow(() -> new AppointmentNotFoundException("Randevu yok"));
        AppointmentResponseDto app = new AppointmentResponseDto();
        app.setId(a.getId());
        app.setUserId(a.getUser().getId());
        app.setUserMail(a.getUser().getEmail());

        return app;
    }

    /**
     * Yeni bir randevu oluşturur
     * @param appointment Oluşturulacak randevu nesnesi
     * @return Kaydedilen randevu
     */
    public Appointment createAppointment(Appointment appointment) {
        // 1. Aynı odada, aynı tarih ve çakışan saatlerde randevu var mı kontrol et
        List<Appointment> existingAppointments = appointmentRepository
                .findByRoomAndDateAndStartTimeLessThanAndEndTimeGreaterThan(
                        appointment.getRoom(),
                        appointment.getDate(),
                        appointment.getEndTime(),
                        appointment.getStartTime()
                )
                .stream()
                .filter(a -> a.getStatus() == Appointment.Status.ACTIVE)
                .toList();


        // 2. Eğer çakışma varsa randevu oluşturulmasın
        if (!existingAppointments.isEmpty()) {
            throw new RuntimeException("Bu odada bu saat aralığında zaten bir randevu var.");
        }

        // 3. Çakışma yoksa randevuyu kaydet
        return appointmentRepository.save(appointment);
    }


    /**
     * Mevcut bir randevuyu günceller
     * @param id Güncellenecek randevunun ID'si
     * @param updatedAppointment Yeni verilerle dolu randevu nesnesi
     * @return Güncellenmiş randevu nesnesi ya da null
     */
    public Appointment updateAppointment(Long id, Appointment updatedAppointment) {
        // Önce ID ile var olan randevuyu bul
        Optional<Appointment> existing = appointmentRepository.findById(id);

        // Randevu varsa güncelle
        if (existing.isPresent()) {
            Appointment a = existing.get();

            // Yeni değerleri set et
            a.setDate(updatedAppointment.getDate());
            a.setStartTime(updatedAppointment.getStartTime());
            a.setEndTime(updatedAppointment.getEndTime());
            a.setRoom(updatedAppointment.getRoom());
            a.setUser(updatedAppointment.getUser());
            a.setStatus(updatedAppointment.getStatus());

            // Güncellenmiş randevuyu kaydet
            return appointmentRepository.save(a);
        } else {
            // Güncellenecek randevu bulunamadıysa null döner
            return null;
        }
    }

    /**
     * Belirli ID'ye sahip randevuyu siler
     * @param id Silinecek randevunun ID'si
     * @return true = silindi, false = bulunamadı
     */
    public boolean deleteAppointment(Long id) {
        // Önce var mı diye kontrol et
        Optional<Appointment> a = appointmentRepository.findById(id);

        if (a.isPresent()) {
            // Varsa sil
            appointmentRepository.deleteById(id);
            return true;

        }

        // Yoksa false döner
        return false;
    }


    public List<Appointment> getAppointmentsByUserId(Long userId) {

        return appointmentRepository.findByUserId(userId);

    }

    public List<Appointment> filterAppointments(LocalDate date, LocalTime startTime, LocalTime endTime, Long roomId){

        Room room = null;

        if(roomId != null){

            room = roomRepository.findById(roomId).orElse(null);

        }
        return appointmentRepository.findAppointmentsByDateAndTimeRange(date, startTime, endTime, room);
    }

}

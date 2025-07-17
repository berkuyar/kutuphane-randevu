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
import java.util.ArrayList;
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
    public List<AppointmentResponseDto> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();

        List<AppointmentResponseDto> dtoList = new ArrayList<>();
        for (Appointment appointment : appointments) {
            AppointmentResponseDto dto = new AppointmentResponseDto();
            dto.setId(appointment.getId());
            dto.setUserId(appointment.getUser().getId());
            dto.setUserName(appointment.getUser().getName());
            dto.setUserEmail(appointment.getUser().getEmail());
            dto.setRoomId(appointment.getRoom().getId());
            dto.setRoomName(appointment.getRoom().getName());
            dto.setDate(appointment.getDate());
            dto.setStartTime(appointment.getStartTime());
            dto.setEndTime(appointment.getEndTime());
            dto.setStatus(appointment.getStatus().name());

            dtoList.add(dto);
        }

        return dtoList;
    }


    /**
     * Belirli ID'ye göre randevuyu getirir
     * @param id Randevunun ID'si
     * @return Optional<Appointment> – olabilir de olmayabilir de
     */
    public AppointmentResponseDto getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Randevu bulunamadı: " + id));

        AppointmentResponseDto dto = new AppointmentResponseDto();
        dto.setId(appointment.getId());
        dto.setUserId(appointment.getUser().getId());
        dto.setUserName(appointment.getUser().getName());
        dto.setUserEmail(appointment.getUser().getEmail());
        dto.setRoomId(appointment.getRoom().getId());
        dto.setRoomName(appointment.getRoom().getName());
        dto.setDate(appointment.getDate());
        dto.setStartTime(appointment.getStartTime());
        dto.setEndTime(appointment.getEndTime());
        dto.setStatus(appointment.getStatus().name());

        return dto;
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
         Appointment existing = appointmentRepository.findById(id)
                 .orElseThrow(() -> new AppointmentNotFoundException("Randevu bulunamadı" +id));

            // Yeni değerleri set et
            existing.setDate(updatedAppointment.getDate());
            existing.setStartTime(updatedAppointment.getStartTime());
            existing.setEndTime(updatedAppointment.getEndTime());
            existing.setRoom(updatedAppointment.getRoom());
            existing.setUser(updatedAppointment.getUser());
            existing.setStatus(updatedAppointment.getStatus());

            // Güncellenmiş randevuyu kaydet
            return appointmentRepository.save(existing);
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

package com.uyarberk.kutuphane_randevu.service;

// Gerekli model ve repository sınıfı içe aktarılır
import com.uyarberk.kutuphane_randevu.dto.AppointmentCreateRequestDto;
import com.uyarberk.kutuphane_randevu.dto.AppointmentResponseDto;
import com.uyarberk.kutuphane_randevu.exception.*;
import com.uyarberk.kutuphane_randevu.model.Appointment;
import com.uyarberk.kutuphane_randevu.model.Room;
import com.uyarberk.kutuphane_randevu.model.User;
import com.uyarberk.kutuphane_randevu.repository.AppointmentRepository;

import com.uyarberk.kutuphane_randevu.repository.RoomRepository;
import com.uyarberk.kutuphane_randevu.repository.UserRepository;
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
    private final UserRepository userRepository;

    // Constructor üzerinden repository enjekte edilir
    public AppointmentService(AppointmentRepository appointmentRepository, RoomRepository roomRepository, UserRepository userRepository) {
        this.appointmentRepository = appointmentRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
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
     *
     * @return Kaydedilen randevu
     */
    public AppointmentResponseDto createAppointment(AppointmentCreateRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException("Kullanıcı bulunamadı: " + dto.getUserId()));

        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException("Oda bulunamadı: " + dto.getRoomId()));

        List<Appointment> existingAppointments = appointmentRepository
                .findByRoomAndDateAndStartTimeLessThanAndEndTimeGreaterThan(
                        room,
                        dto.getDate(),
                        dto.getEndTime(),
                        dto.getStartTime()
                )
                .stream()
                .filter(a -> a.getStatus() == Appointment.Status.ACTIVE)
                .toList();

        if (!existingAppointments.isEmpty()) {
            throw new RuntimeException("Bu odada bu saat aralığında zaten bir randevu var.");
        }
        if(dto.getDate().isBefore(LocalDate.now())) {
            throw new PastDateAppointmentException("Geçmiş tarihe randevu alınamaz.");
        }
        if (dto.getDate().isEqual(LocalDate.now()) && dto.getStartTime().isBefore(LocalTime.now())) {
            throw new PastDateAppointmentException("Geçmiş saate randevu alınamaz.");
        }
        if(!dto.getEndTime().isAfter(dto.getStartTime())) {
            throw new InvalidAppointmentTimeException("Bitiş saati başlangıç saatinden sonra olmalıdır.");
        }

        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setRoom(room);
        appointment.setDate(dto.getDate());
        appointment.setStartTime(dto.getStartTime());
        appointment.setEndTime(dto.getEndTime());
        appointment.setStatus(Appointment.Status.ACTIVE);

        Appointment saved = appointmentRepository.save(appointment);

        return new AppointmentResponseDto(
                user.getName(),
                saved.getId(),
                user.getId(),
                user.getEmail(),
                room.getId(),
                room.getName(),
                saved.getDate(),
                saved.getStartTime(),
                saved.getEndTime(),
                saved.getStatus().name()
        );
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

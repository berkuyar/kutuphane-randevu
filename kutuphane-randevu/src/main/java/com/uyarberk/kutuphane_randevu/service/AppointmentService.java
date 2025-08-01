package com.uyarberk.kutuphane_randevu.service;

// Gerekli model ve repository sınıfı içe aktarılır
import com.uyarberk.kutuphane_randevu.dto.*;
import com.uyarberk.kutuphane_randevu.exception.*;
import com.uyarberk.kutuphane_randevu.model.Appointment;
import com.uyarberk.kutuphane_randevu.model.Room;
import com.uyarberk.kutuphane_randevu.model.User;
import com.uyarberk.kutuphane_randevu.repository.AppointmentRepository;

import com.uyarberk.kutuphane_randevu.repository.RoomRepository;
import com.uyarberk.kutuphane_randevu.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Slf4j
@Service // Bu sınıf bir servis bileşenidir, Spring tarafından yönetilir
public class AppointmentService {

    // Appointment işlemleri için repository'e ihtiyacımız var
    private final AppointmentRepository appointmentRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final AppointmentPeriodService appointmentPeriodService;
    private final TimeSlotService timeSlotService;

    // Constructor üzerinden repository enjekte edilir
    public AppointmentService(AppointmentRepository appointmentRepository, RoomRepository roomRepository, UserRepository userRepository, NotificationService notificationService, AppointmentPeriodService appointmentPeriodService, TimeSlotService timeSlotService) {
        this.appointmentRepository = appointmentRepository;
        this.roomRepository = roomRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.appointmentPeriodService = appointmentPeriodService;
        this.timeSlotService = timeSlotService;
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
        log.info("ID ile randevu getirme isteği alındı: appointmentId={} ", id);
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() ->{
                log.error("Randevu bulunamadı: appointmentId={} ", id);
        return new AppointmentNotFoundException("Randevu bulunamadı: " + id);
    });
        log.info("Randevu başarıyla bulundu.appointmentId={} ", id);

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



    public AppointmentResponseDto createAppointment(AppointmentCreateRequestDto dto) {
        log.info("Randevu oluşturma isteği: userId={}, roomId={}, date={}, startTime={}, endTime={}",
                dto.getUserId(), dto.getRoomId(), dto.getDate(), dto.getStartTime(), dto.getEndTime());

        User user = userRepository.findById(dto.getUserId()) .orElseThrow(() -> {
            log.error("Kullanıcı bulunamadı: userId={}", dto.getUserId());
            return new UserNotFoundException("Kullanıcı bulunamadı: " + dto.getUserId());
        });

        Room room = roomRepository.findById(dto.getRoomId()).orElseThrow(() -> {
        log.error("Oda bulunamadı. roomId={}", dto.getRoomId());
        return new RoomNotFoundException("Oda bulunamadı: " + dto.getRoomId());
        });

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
            log.warn("Randevu çakışması: odaId={}, tarih={}, startTime={}, endTime={}",
                    dto.getRoomId(), dto.getDate(), dto.getStartTime(), dto.getEndTime());
            throw new RuntimeException("Bu odada bu saat aralığında zaten bir randevu var.");
        }

        if (dto.getDate().isBefore(LocalDate.now())) {
            log.warn("Geçmiş tarihe randevu oluşturma isteği. date={}", dto.getDate());
            throw new PastDateAppointmentException("Geçmiş tarihe randevu alınamaz.");
        }

        if (dto.getDate().isEqual(LocalDate.now()) && dto.getStartTime().isBefore(LocalTime.now())) {
            log.warn("Geçmiş saate randevu oluşturma isteği. date={}, startTime={}", dto.getDate(), dto.getStartTime());
            throw new PastDateAppointmentException("Geçmiş saate randevu alınamaz.");
        }

        if (!dto.getEndTime().isAfter(dto.getStartTime())) {
            log.warn("Geçersiz randevu saati: startTime={}, endTime={}", dto.getStartTime(), dto.getEndTime());
            throw new InvalidAppointmentTimeException("Bitiş saati başlangıç saatinden sonra olmalıdır.");
        }

        if (!appointmentPeriodService.isAppointmentAllowedForDate(dto.getDate(), dto.getStartTime(), dto.getEndTime())) {
            log.warn("Randevu için uygun dönem bulunamadı: date={}, startTime={}, endTime={}", 
                    dto.getDate(), dto.getStartTime(), dto.getEndTime());
            throw new RuntimeException("Bu tarih ve saat aralığı için randevu alınamaz. Lütfen admin tarafından belirlenen randevu dönemlerini kontrol edin.");
        }

        if (!timeSlotService.isValidSlot(dto.getStartTime(), dto.getEndTime())) {
            log.warn("Geçersiz zaman slotu: startTime={}, endTime={}", dto.getStartTime(), dto.getEndTime());
            throw new RuntimeException("Bu saat aralığı için tanımlı bir slot bulunmamaktadır. Lütfen mevcut slotlardan birini seçin.");
        }

        Appointment appointment = new Appointment();
        appointment.setUser(user);
        appointment.setRoom(room);
        appointment.setDate(dto.getDate());
        appointment.setStartTime(dto.getStartTime());
        appointment.setEndTime(dto.getEndTime());
        appointment.setStatus(Appointment.Status.ACTIVE);

        Appointment saved = appointmentRepository.save(appointment);
        log.info("Randevu başarıyla oluşturuldu: appointmentId={}", saved.getId());

        //kullanıcıya bildirim gönder.
        notificationService.createNotification(
                "Yeni randevunuz oluşturuldu: " + saved.getDate()+ " " + saved.getStartTime() + " - " + saved.getEndTime(),
                user.getId()
        );

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
    public AppointmentUpdateRepsonseDto updateAppointment(Long id, AppointmentUpdateRequestDto updatedAppointment, Long userId) {

        Appointment appointment = appointmentRepository.findById(id).orElseThrow(()->{
            log.error("Güncellemek istediğiniz randevu bulunamadı. appointmentId={}", id);
            return new AppointmentNotFoundException("Randevu bulunamadı.");
        });
        
        // Kullanıcının sadece kendi randevusunu güncelleyebilmesini kontrol et
        if (!appointment.getUser().getId().equals(userId)) {
            log.error("Kullanıcı başkasının randevusunu güncellemeye çalışıyor. userId={}, appointmentUserId={}", userId, appointment.getUser().getId());
            throw new UnauthorizeNotFoundException("Bu randevuyu güncelleme yetkiniz yok.");
        }
         List<Appointment> existingAppointments = appointmentRepository.findConflictingAppointments(
                 updatedAppointment.getRoomId(),
                 updatedAppointment.getDate(),
                 updatedAppointment.getStartTime(),
                 updatedAppointment.getEndTime(),
                 id
         );
         if(!existingAppointments.isEmpty()) {
             log.warn("Randevu çakışması. roomId={}, date={}, time={}");
             updatedAppointment.getRoomId();
             updatedAppointment.getDate();
             updatedAppointment.getStartTime();
             updatedAppointment.getEndTime();
             throw new RuntimeException("Bu odada bu saat aralığında zaten bir randevu var.");
         }
        appointment.setDate(updatedAppointment.getDate());
         appointment.setStartTime(updatedAppointment.getStartTime());
         appointment.setEndTime(updatedAppointment.getEndTime());
        Room room = roomRepository.findById(updatedAppointment.getRoomId())
                .orElseThrow(() -> new RoomNotFoundException("Oda bulunamadı"));
        appointment.setRoom(room);
        appointmentRepository.save(appointment);
         log.info("Randevu başarıyla güncellendi.");
         notificationService.createNotification(
                 "Randevunuz iptal edildi. " +
                         appointment.getDate() + " " +
                         appointment.getStartTime() + " - " +
                         appointment.getEndTime(),
                         appointment.getUser().getId()

                 );


         AppointmentUpdateRepsonseDto appointmentUpdateRepsonseDto = new AppointmentUpdateRepsonseDto();
         appointmentUpdateRepsonseDto.setDate(appointment.getDate());
         appointmentUpdateRepsonseDto.setStartTime(appointment.getStartTime());
         appointmentUpdateRepsonseDto.setEndTime(appointment.getEndTime());
         appointmentUpdateRepsonseDto.setRoomName(appointment.getRoom().getName());

         return  appointmentUpdateRepsonseDto;
        }

    /**
     * Belirli ID'ye sahip randevuyu siler
     * @param id Silinecek randevunun ID'si
     * @param userId Silme işlemini yapan kullanıcının ID'si
     * @param userRole Kullanıcının rolü (USER/ADMIN)
     * @return true = silindi, false = bulunamadı
     */
    public boolean deleteAppointment(Long id, Long userId, String userRole) {
        // Önce var mı diye kontrol et
        Appointment appointment = appointmentRepository.findById(id).orElseThrow(() -> {
            log.error("Silinecek randevu bulunamadı. appointmentId={}", id);
            return new AppointmentNotFoundException("Randevu bulunamadı.");
        });

        // Kullanıcının sadece kendi randevusunu silebilmesini kontrol et (Admin hariç)
        if (!userRole.equals("ADMIN") && !appointment.getUser().getId().equals(userId)) {
            log.error("Kullanıcı başkasının randevusunu silmeye çalışıyor. userId={}, appointmentUserId={}", userId, appointment.getUser().getId());
            throw new UnauthorizeNotFoundException("Bu randevuyu silme yetkiniz yok.");
        }

        // ✅ Silmeden önce bildirim oluştur
        String message = "Randevunuz iptal edildi: " +
                appointment.getDate() + " " +
                appointment.getStartTime() + " - " +
                appointment.getEndTime();

        Long appointmentOwnerId = appointment.getUser().getId();

        // ✅ Bildirim gönder
        notificationService.createNotification(message, appointmentOwnerId);

        // Randevuyu sil
        appointmentRepository.delete(appointment);
        log.info("Randevu başarıyla silindi. appointmentId={}, deletedBy={}", id, userId);

        return true;
    }

    public List<AppointmentByUserIdResponseDto> getAppointmentsByUserId(Long userId) {
          List<Appointment> appointments = appointmentRepository.findByUserId(userId);

          if(appointments.isEmpty()) {
              log.info("Bu kullanıcıya ait hiç randevu yok. userId={}", userId);
              return new ArrayList<>(); // Boş liste döndür, exception fırlatma
          }
          List<AppointmentByUserIdResponseDto> appointmentByUserIdResponseDtoList = new ArrayList<>();
          for(Appointment appointment : appointments) {
              AppointmentByUserIdResponseDto appointmentByUserIdResponseDto = new AppointmentByUserIdResponseDto();
              appointmentByUserIdResponseDto.setId(appointment.getId()); // ID set ediliyor
              appointmentByUserIdResponseDto.setDate(appointment.getDate());
              appointmentByUserIdResponseDto.setStartTime(appointment.getStartTime());
              appointmentByUserIdResponseDto.setEndTime(appointment.getEndTime());
              appointmentByUserIdResponseDto.setRoomName(appointment.getRoom().getName());
              appointmentByUserIdResponseDto.setUserName(appointment.getUser().getName());
              appointmentByUserIdResponseDtoList.add(appointmentByUserIdResponseDto);
          }
           return  appointmentByUserIdResponseDtoList;
    }

    public List<Appointment> filterAppointments(LocalDate date, LocalTime startTime, LocalTime endTime, Long roomId){

        Room room = null;

        if(roomId != null){

            room = roomRepository.findById(roomId).orElse(null);

        }
        return appointmentRepository.findAppointmentsByDateAndTimeRange(date, startTime, endTime, room);
    }
    public List<Appointment> getAllAppointmentsByDate(LocalDate date){
        return appointmentRepository.findByDateAndStatus(date, Appointment.Status.ACTIVE);
    }

    public List<RoomPopularityDto> getMostPopularRooms(LocalDate startDate, LocalDate endDate) {
        log.info("En popüler odalar sorgusu: startDate={}, endDate={}", startDate, endDate);
        return appointmentRepository.findMostPopularRooms(startDate, endDate);
    }

    public List<WeeklyOccupancyDto> getWeeklyOccupancyPattern(LocalDate startDate, LocalDate endDate) {
        log.info("Haftalık doluluk analizi sorgusu: startDate={}, endDate={}", startDate, endDate);
        return appointmentRepository.findWeeklyOccupancyPattern(startDate, endDate);
    }

}

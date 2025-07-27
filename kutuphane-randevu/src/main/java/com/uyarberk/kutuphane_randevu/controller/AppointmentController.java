package com.uyarberk.kutuphane_randevu.controller;

import com.uyarberk.kutuphane_randevu.dto.*;
import com.uyarberk.kutuphane_randevu.model.Appointment;
import com.uyarberk.kutuphane_randevu.model.User;
import com.uyarberk.kutuphane_randevu.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
@Slf4j
@RestController // Bu sınıf bir REST API denetleyicisidir
@RequestMapping("/api/appointments") // Tüm endpointler /api/appointments ile başlar
public class AppointmentController {

    private final AppointmentService appointmentService;

    // Constructor ile servis sınıfı enjekte edilir
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Tüm randevuları getir (GET /api/appointments)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AppointmentResponseDto>> getAllAppointments() {
        log.info("Tüm randevuları getirme isteği alındı.");
        List<AppointmentResponseDto> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments); // HTTP 200 + liste döner
    }

    // Belirli ID'ye göre randevu getir (GET /api/appointments/{id})
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponseDto> getAppointmentById(@PathVariable Long id) {
        log.info("Randevu getirme isteği alındı. appointmentId={}", id);
        AppointmentResponseDto appointment = appointmentService.getAppointmentById(id);

        return ResponseEntity.ok(appointment);

    }
    // Bu endpoint'e sadece 'USER' veya 'ADMIN' rolüne sahip kullanıcılar erişebilir
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
// HTTP GET isteği ile /api/appointments/my endpoint'ine istek atıldığında bu metod çalışır
    @GetMapping("/my")
    public ResponseEntity<List<AppointmentByUserIdResponseDto>> getMyAppointments(Authentication authentication) {
        // authentication nesnesi Spring Security tarafından otomatik sağlanır.
        // JWT token doğrulandıysa, içindeki kullanıcı bilgileri buradan alınabilir.
        // authentication.getPrincipal() → token'dan gelen kullanıcı nesnesidir.
        // Bu nesne doğrudan bizim User entity'si olarak ayarlanmış durumda.
        User user = (User) authentication.getPrincipal();
        log.info("Kullanıcıya ait randevular getiriliyor. userID={}", user.getId());

        // Kullanıcının ID'sine göre kendi randevularını getir
        List<AppointmentByUserIdResponseDto> appointments = appointmentService.getAppointmentsByUserId(user.getId());


        // Randevuları 200 OK HTTP cevabıyla birlikte JSON olarak geri döndür
        return ResponseEntity.ok(appointments);
    }

    // Yeni randevu oluştur (POST /api/appointments)
    @PostMapping
    public ResponseEntity<AppointmentResponseDto> createAppointment(@RequestBody @Valid AppointmentCreateRequestDto dto) {
        log.info("Randevu oluşturma isteği alındı.");
        AppointmentResponseDto response = appointmentService.createAppointment(dto);
        return ResponseEntity.ok(response);
    }



    // Var olan randevuyu güncelle (PUT /api/appointments/{id})
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<AppointmentUpdateRepsonseDto> updateAppointment(@PathVariable Long id, @RequestBody AppointmentUpdateRequestDto updatedAppointment) {
        log.info("Randevu güncelleme isteği alındı. appointmentId={}", id);
        AppointmentUpdateRepsonseDto appointmentUpdateRepsonseDto = appointmentService.updateAppointment(id, updatedAppointment);
         return ResponseEntity.ok(appointmentUpdateRepsonseDto);
    }

    // Randevuyu sil (DELETE /api/appointments/{id})
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
        log.info("Randevu silme isteği alındı. appointmentId={}", id);
         appointmentService.deleteAppointment(id);
         log.info("Randevu başarıyla silindi. appointmentId={}", id);
       return ResponseEntity.ok("Randevu başarıyla silindi.");
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AppointmentByUserIdResponseDto>> getAppointmentsByUserId(@PathVariable("userId") Long userId){
        log.info("Kullanıcının randevularını getirme isteği alındı. userID={}", userId);
        List<AppointmentByUserIdResponseDto> appointmentByUserIdResponseDtoList = appointmentService.getAppointmentsByUserId(userId);
        return ResponseEntity.ok(appointmentByUserIdResponseDtoList);
    }
@PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/filter")
    public ResponseEntity<List<Appointment>> filterAppointments(
        @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
        @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
        @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime,
        @RequestParam(value = "roomId", required = false) Long roomId
){

       List<Appointment> result = appointmentService.filterAppointments(date, startTime, endTime, roomId);
       return ResponseEntity.ok(result);
}

@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/popular-rooms")
public ResponseEntity<List<RoomPopularityDto>> getMostPopularRooms(
    @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
    @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
) {
    log.info("En popüler odalar sorgusu: startDate={}, endDate={}", startDate, endDate);
    List<RoomPopularityDto> popularRooms = appointmentService.getMostPopularRooms(startDate, endDate);
    return ResponseEntity.ok(popularRooms);
}

@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/weekly-occupancy")
public ResponseEntity<List<WeeklyOccupancyDto>> getWeeklyOccupancyPattern(
    @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
    @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
) {
    log.info("Haftalık doluluk analizi sorgusu: startDate={}, endDate={}", startDate, endDate);
    List<WeeklyOccupancyDto> weeklyPattern = appointmentService.getWeeklyOccupancyPattern(startDate, endDate);
    return ResponseEntity.ok(weeklyPattern);
}

}

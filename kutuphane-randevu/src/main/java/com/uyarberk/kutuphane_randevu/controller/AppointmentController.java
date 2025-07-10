package com.uyarberk.kutuphane_randevu.controller;

import com.uyarberk.kutuphane_randevu.model.Appointment;
import com.uyarberk.kutuphane_randevu.model.User;
import com.uyarberk.kutuphane_randevu.service.AppointmentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments); // HTTP 200 + liste döner
    }

    // Belirli ID'ye göre randevu getir (GET /api/appointments/{id})
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);

        if (appointment.isPresent()) {
            return ResponseEntity.ok(appointment.get()); // HTTP 200 + randevu
        } else {
            return ResponseEntity.notFound().build(); // HTTP 404
        }
    }
    // Bu endpoint'e sadece 'USER' veya 'ADMIN' rolüne sahip kullanıcılar erişebilir
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
// HTTP GET isteği ile /api/appointments/my endpoint'ine istek atıldığında bu metod çalışır
    @GetMapping("/my")
    public ResponseEntity<List<Appointment>> getMyAppointments(Authentication authentication) {

        // authentication nesnesi Spring Security tarafından otomatik sağlanır.
        // JWT token doğrulandıysa, içindeki kullanıcı bilgileri buradan alınabilir.
        // authentication.getPrincipal() → token'dan gelen kullanıcı nesnesidir.
        // Bu nesne doğrudan bizim User entity'si olarak ayarlanmış durumda.
        User user = (User) authentication.getPrincipal();

        // Kullanıcının ID'sine göre kendi randevularını getir
        List<Appointment> appointments = appointmentService.getAppointmentsByUserId(user.getId());

        // Randevuları 200 OK HTTP cevabıyla birlikte JSON olarak geri döndür
        return ResponseEntity.ok(appointments);
    }

    // Yeni randevu oluştur (POST /api/appointments)
    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody Appointment appointment) {
        try {
            Appointment created = appointmentService.createAppointment(appointment);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    // Var olan randevuyu güncelle (PUT /api/appointments/{id})
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable Long id, @RequestBody Appointment updatedAppointment) {
        Appointment updated = appointmentService.updateAppointment(id, updatedAppointment);

        if (updated != null) {
            return ResponseEntity.ok(updated); // HTTP 200 + güncellenmiş veri
        } else {
            return ResponseEntity.notFound().build(); // HTTP 404
        }
    }

    // Randevuyu sil (DELETE /api/appointments/{id})
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
       boolean deleted = appointmentService.deleteAppointment(id);
       if(deleted){
           return ResponseEntity.ok("Randevu Silindi.");
       }else{
           return ResponseEntity.status(404).body("Kullanıcı Bulunamadı");

       }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByUserId(@PathVariable("userId") Long userId){
    List<Appointment> appointments = appointmentService.getAppointmentsByUserId(userId);
        return ResponseEntity.ok(appointments);
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


}

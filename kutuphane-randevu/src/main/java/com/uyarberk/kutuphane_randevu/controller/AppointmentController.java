package com.uyarberk.kutuphane_randevu.controller;

import com.uyarberk.kutuphane_randevu.model.Appointment;
import com.uyarberk.kutuphane_randevu.service.AppointmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        return ResponseEntity.ok(appointments); // HTTP 200 + liste döner
    }

    // Belirli ID'ye göre randevu getir (GET /api/appointments/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable Long id) {
        Optional<Appointment> appointment = appointmentService.getAppointmentById(id);

        if (appointment.isPresent()) {
            return ResponseEntity.ok(appointment.get()); // HTTP 200 + randevu
        } else {
            return ResponseEntity.notFound().build(); // HTTP 404
        }
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
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAppointment(@PathVariable Long id) {
       boolean deleted = appointmentService.deleteAppointment(id);
       if(deleted){
           return ResponseEntity.ok("Randevu Silindi.");
       }else{
           return ResponseEntity.status(404).body("Kullanıcı Bulunamadı");

       }
    }
    @GetMapping("/user{userId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByUserId(@PathVariable Long userId){
        List<Appointment> appointments = appointmentService.getAppointmentsByUserId(userId);
        return ResponseEntity.ok(appointments);
    }
}

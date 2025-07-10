package com.uyarberk.kutuphane_randevu.controller;

import com.uyarberk.kutuphane_randevu.model.Room;
import com.uyarberk.kutuphane_randevu.service.RoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController // Bu sınıf bir REST API'dir
@RequestMapping("/api/rooms") // Tüm istekler /api/rooms ile başlar
public class RoomController {

    private final RoomService roomService;

    // Servisi constructor üzerinden alıyoruz (bağımlılık enjeksiyonu)
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Tüm odaları getir (GET /api/rooms)
    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomService.getAllRooms();

        // Başarılıysa HTTP 200 + oda listesi döner
        return ResponseEntity.ok(rooms);
    }

    // ID ile tek bir oda getir (GET /api/rooms/{id})
    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoomById(@PathVariable Long id) {
        // Servis üzerinden odayı arıyoruz
        Optional<Room> room = roomService.getRoomById(id);

        // Oda varsa HTTP 200 döner
        if (room.isPresent()) {
            return ResponseEntity.ok(room.get());
        }

        // Oda yoksa HTTP 404 Not Found döner
        return ResponseEntity.notFound().build();
    }

    // Yeni oda oluştur (POST /api/rooms)
    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        // Gelen Room objesini veritabanına kaydeder
        Room createdRoom = roomService.createRoom(room);

        // Kaydedilen yeni odayı HTTP 200 ile döner
        return ResponseEntity.ok(createdRoom);
    }

    // Odayı güncelle (PUT /api/rooms/{id})
    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable Long id, @RequestBody Room updatedRoom) {
        // Servis üzerinden güncelleme yapılır
        Room updated = roomService.updateRoom(id, updatedRoom);

        // Güncelleme başarılıysa HTTP 200 döner
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }

        // Oda bulunamazsa HTTP 404 döner
        return ResponseEntity.notFound().build();
    }

    // Odayı sil (DELETE /api/rooms/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        // Servis üzerinden silme işlemi yapılır
        roomService.deleteRoom(id);

        // Başarılıysa HTTP 200 ve mesaj döner
        return ResponseEntity.ok("Oda silindi");
    }
}

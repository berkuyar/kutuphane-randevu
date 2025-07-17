package com.uyarberk.kutuphane_randevu.controller;

import com.uyarberk.kutuphane_randevu.dto.RoomCreateRequestDto;
import com.uyarberk.kutuphane_randevu.dto.RoomDto;
import com.uyarberk.kutuphane_randevu.dto.RoomResponseDto;
import com.uyarberk.kutuphane_randevu.dto.RoomUpdateRequestDto;
import com.uyarberk.kutuphane_randevu.model.Room;
import com.uyarberk.kutuphane_randevu.service.RoomService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
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
    public ResponseEntity<List<RoomResponseDto>> getAllRooms() {
        List<RoomResponseDto> roomList= roomService.getAllRooms();

        // Başarılıysa HTTP 200 + oda listesi döner
        return ResponseEntity.ok(roomList);
    }

    // ID ile tek bir oda getir (GET /api/rooms/{id})
    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long id) {
        RoomDto roomDto = roomService.getRoomById(id);
        return ResponseEntity.ok(roomDto);

    }

    // Yeni oda oluştur (POST /api/rooms)
    @PreAuthorize("hasRole ('ADMIN')")
    @PostMapping
    public ResponseEntity<RoomResponseDto> createRoom(@RequestBody RoomCreateRequestDto roomCreateRequestDto) {

        RoomResponseDto responseDto = roomService.createRoom(roomCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    // Odayı güncelle (PUT /api/rooms/{id})
    @PreAuthorize("hasRole ('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDto> updateRoom(@PathVariable Long id, @RequestBody RoomUpdateRequestDto requestDto) {
        // Servis üzerinden güncelleme yapılır
        RoomResponseDto responseDto = roomService.updateRoom(id, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // Odayı sil (DELETE /api/rooms/{id})
    @PreAuthorize("hasRole ('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
      roomService.deleteRoom(id);
        return ResponseEntity.ok("Oda silindi");
    }
    }


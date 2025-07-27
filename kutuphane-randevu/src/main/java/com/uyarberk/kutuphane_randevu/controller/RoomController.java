package com.uyarberk.kutuphane_randevu.controller;

import com.uyarberk.kutuphane_randevu.dto.RoomCreateRequestDto;
import com.uyarberk.kutuphane_randevu.dto.RoomDto;
import com.uyarberk.kutuphane_randevu.dto.RoomResponseDto;
import com.uyarberk.kutuphane_randevu.dto.RoomUpdateRequestDto;
import com.uyarberk.kutuphane_randevu.service.RoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000") // React için CORS izni
@Slf4j
@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }
    @PreAuthorize("permitAll()")
    @GetMapping
    public ResponseEntity<List<RoomResponseDto>> getAllRooms() {
        log.info("Tüm odaları getirme isteği alındı.");
        List<RoomResponseDto> roomList = roomService.getAllRooms();
        log.info("Toplam {} oda bulundu.", roomList.size());
        return ResponseEntity.ok(roomList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDto> getRoomById(@PathVariable Long id) {
        log.info("Oda getirme isteği. roomId={}", id);
        RoomDto roomDto = roomService.getRoomById(id);
        log.info("Oda bulundu. roomId={}, roomName={}", id, roomDto.getName());
        return ResponseEntity.ok(roomDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RoomResponseDto> createRoom(@RequestBody RoomCreateRequestDto roomCreateRequestDto) {
        log.info("Yeni oda oluşturma isteği alındı. roomName={}", roomCreateRequestDto.getName());
        RoomResponseDto responseDto = roomService.createRoom(roomCreateRequestDto);
        log.info("Oda başarıyla oluşturuldu. roomId={}, roomName={}", responseDto.getId(), responseDto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponseDto> updateRoom(@PathVariable Long id, @RequestBody RoomUpdateRequestDto requestDto) {
        log.info("Oda güncelleme isteği. roomId={}, yeni ad={}", id, requestDto.getName());
        RoomResponseDto responseDto = roomService.updateRoom(id, requestDto);
        log.info("Oda başarıyla güncellendi. roomId={}", id);
        return ResponseEntity.ok(responseDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRoom(@PathVariable Long id) {
        log.info("Oda silme isteği alındı. roomId={}", id);
        roomService.deleteRoom(id);
        log.info("Oda başarıyla silindi. roomId={}", id);
        return ResponseEntity.ok("Oda silindi");
    }
}

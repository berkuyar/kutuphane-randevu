package com.uyarberk.kutuphane_randevu.service;

import com.uyarberk.kutuphane_randevu.dto.RoomCreateRequestDto;
import com.uyarberk.kutuphane_randevu.dto.RoomDto;
import com.uyarberk.kutuphane_randevu.dto.RoomResponseDto;
import com.uyarberk.kutuphane_randevu.dto.RoomUpdateRequestDto;
import com.uyarberk.kutuphane_randevu.exception.DuplicateRoomException;
import com.uyarberk.kutuphane_randevu.exception.RoomNotFoundException;
import com.uyarberk.kutuphane_randevu.model.Room;
import com.uyarberk.kutuphane_randevu.repository.RoomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * RoomService sınıfı, odalarla ilgili iş mantığını içerir.
 * Veritabanı işlemleri doğrudan burada değil, repository aracılığıyla yapılır.
 * Bu katman controller ile repository arasında köprü görevi görür.
 */
@Slf4j
@Service
public class RoomService {


    // Room veritabanı işlemleri için RoomRepository kullanılır.
    private final RoomRepository roomRepository;

    /**
     * Constructor-based dependency injection.
     * Spring, RoomRepository bean'ini buraya otomatik olarak enjekte eder.
     */
    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Tüm odaları getirir.
     *
     * @return Tüm oda kayıtlarının listesi
     */
    public List<RoomResponseDto> getAllRooms() {
        List<Room> rooms = roomRepository.findAll();
        log.info("Tüm odaları getirme isteği alındı.");
        List<RoomResponseDto> dtoList = new ArrayList<>();
        for (Room room : rooms){
            RoomResponseDto dto = new RoomResponseDto();
            dto.setId(room.getId());
            dto.setName(room.getName());
            dto.setDescription(room.getDescription());
            dto.setCapacity(room.getCapacity());
            dtoList.add(dto);
        }

        return dtoList;
    }


    /**
     * Belirli ID'ye sahip odayı getirir.
     *
     * @param id Odanın veritabanı ID'si
     * @return Eğer varsa Room nesnesi, yoksa boş Optional
     */
    public RoomDto getRoomById(Long id) {
        log.info("Id'ye göre oda getirme isteği alındı. roomId={}", id);
          Room room = roomRepository.findById(id).orElseThrow(()->{

                  log.error("Oda bulunamadı. roomId={}", id);
                return new RoomNotFoundException("Oda bulunamadı");
          });
          log.info("Oda başarıyla bulundu. roomId={}", id);
          RoomDto dto = new RoomDto();
        dto.setId(room.getId());
        dto.setName(room.getName());
        dto.setDescription(room.getDescription());
        dto.setCapacity(room.getCapacity());

        return dto;
    }

    /**
     * Yeni bir oda kaydeder.
     *
     *
     * @return Kaydedilen Room
     */
    public RoomResponseDto createRoom(RoomCreateRequestDto dto) {
        log.info("Yeni oda oluşturma isteği alındı: {}" , dto.getName());

        boolean odaVarmi = roomRepository.existsByName(dto.getName());
        if(odaVarmi){
            log.warn("Aynı isimde bir oda zaten var: {}", dto.getName());
            throw new DuplicateRoomException("Bu isimde bir oda zaten var: " + dto.getName());
        }
        Room room = new Room();
        room.setName(dto.getName());
        room.setCapacity(dto.getCapacity());
        room.setDescription(dto.getDescription());
        Room saved = roomRepository.save(room);
        log.info("Yeni oda başarıyla kaydedildi: {}", saved.getId());
        RoomResponseDto response = new RoomResponseDto();
        response.setId(saved.getId());
        response.setName(saved.getName());
        response.setDescription(saved.getDescription());
        response.setCapacity(saved.getCapacity());

        return response;
    }

    /**
     * Var olan bir odayı günceller.
     *
     * @param id Güncellenecek odanın ID'si
     * @return Güncellenmiş Room nesnesi, bulunamazsa null
     */
    public RoomResponseDto updateRoom(Long id, RoomUpdateRequestDto requestDto) {
        log.info("Oda güncelleme isteği alındı. roomId={}", id);
    Room room = roomRepository.findById(id).orElseThrow(() -> {
        log.error("Güncellenecek oda bulunamadı. roomId={}", id);
         return new RoomNotFoundException("Güncellenecek Oda Bulunamadı");
    });
    room.setName(requestDto.getName());
    room.setDescription(requestDto.getDescription());
    room.setCapacity(requestDto.getCapacity());
    Room updated = roomRepository.save(room);
      log.info("Oda başarıyla güncellendi. roomId={}", updated.getId());
        RoomResponseDto dto = new RoomResponseDto();
        dto.setId(updated.getId());
        dto.setName(updated.getName());
        dto.setCapacity(updated.getCapacity());
        dto.setDescription(updated.getDescription());

        return dto;
    }

    /**
     * Belirli ID'ye sahip odayı siler.
     *
     * @param id Silinecek odanın ID'si
     */
    public void deleteRoom(Long id) {
        log.info("Oda silinme isteği alındı. roomId={}", id);

        if (!roomRepository.existsById(id)) {
            log.error("Silinecek oda bulunamadı. roomId={} ", id);
            throw new RoomNotFoundException("Silinmek istenen oda bulunamadı");
        }
        roomRepository.deleteById(id);
        log.info("Oda başarıyla silindi. roomId={}", id);
    }

}

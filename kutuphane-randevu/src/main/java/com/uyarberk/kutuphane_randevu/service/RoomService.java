package com.uyarberk.kutuphane_randevu.service;

import com.uyarberk.kutuphane_randevu.exception.RoomNotFoundException;
import com.uyarberk.kutuphane_randevu.model.Room;
import com.uyarberk.kutuphane_randevu.repository.RoomRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * RoomService sınıfı, odalarla ilgili iş mantığını içerir.
 * Veritabanı işlemleri doğrudan burada değil, repository aracılığıyla yapılır.
 * Bu katman controller ile repository arasında köprü görevi görür.
 */
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
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    /**
     * Belirli ID'ye sahip odayı getirir.
     *
     * @param id Odanın veritabanı ID'si
     * @return Eğer varsa Room nesnesi, yoksa boş Optional
     */
    public Room getRoomById(Long id) {

        return roomRepository.findById(id).orElseThrow(()-> new RoomNotFoundException("Oda bulunamadı!"));
    }

    /**
     * Yeni bir oda kaydeder.
     *
     * @param room Yeni oluşturulacak Room nesnesi
     * @return Kaydedilen Room
     */
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    /**
     * Var olan bir odayı günceller.
     *
     * @param id Güncellenecek odanın ID'si
     * @param updatedRoom Yeni verilerle dolu Room nesnesi
     * @return Güncellenmiş Room nesnesi, bulunamazsa null
     */
    public Room updateRoom(Long id, Room updatedRoom) {
        Optional<Room> optionalRoom = roomRepository.findById(id);
        if (optionalRoom.isPresent()) {
            Room room = optionalRoom.get();
            room.setName(updatedRoom.getName());
            room.setDescription(updatedRoom.getDescription());
            room.setCapacity(updatedRoom.getCapacity());
            room.setCreatedBy(updatedRoom.getCreatedBy());
            return roomRepository.save(room);
        } else {
            // Oda bulunamazsa null döndürülür (alternatif: exception fırlatılabilir)
            return null;
        }
    }

    /**
     * Belirli ID'ye sahip odayı siler.
     *
     * @param id Silinecek odanın ID'si
     */
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}

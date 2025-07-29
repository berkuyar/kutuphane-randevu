package com.uyarberk.kutuphane_randevu.service;

import com.uyarberk.kutuphane_randevu.dto.TimeSlotCreateRequestDto;
import com.uyarberk.kutuphane_randevu.dto.TimeSlotResponseDto;
import com.uyarberk.kutuphane_randevu.dto.AvailableSlotDto;
import com.uyarberk.kutuphane_randevu.exception.TimeSlotNotFoundException;
import com.uyarberk.kutuphane_randevu.model.TimeSlot;
import com.uyarberk.kutuphane_randevu.model.Appointment;
import com.uyarberk.kutuphane_randevu.repository.TimeSlotRepository;
import com.uyarberk.kutuphane_randevu.repository.AppointmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class TimeSlotService {

    private final TimeSlotRepository timeSlotRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentPeriodService appointmentPeriodService;

    public TimeSlotService(TimeSlotRepository timeSlotRepository, AppointmentRepository appointmentRepository, AppointmentPeriodService appointmentPeriodService) {
        this.timeSlotRepository = timeSlotRepository;
        this.appointmentRepository = appointmentRepository;
        this.appointmentPeriodService = appointmentPeriodService;
    }

    public TimeSlotResponseDto createSlot(TimeSlotCreateRequestDto dto) {
        log.info("Yeni zaman slotu oluşturuluyor: başlangıç={}, bitiş={}", dto.getStartTime(), dto.getEndTime());

        if (dto.getEndTime().isBefore(dto.getStartTime()) || dto.getEndTime().equals(dto.getStartTime())) {
            throw new IllegalArgumentException("Bitiş saati başlangıç saatinden sonra olmalıdır.");
        }

        List<TimeSlot> overlappingSlots = timeSlotRepository.findOverlappingSlots(dto.getStartTime(), dto.getEndTime());
        if (!overlappingSlots.isEmpty()) {
            throw new IllegalArgumentException("Bu saat aralığında zaten bir slot mevcut.");
        }

        TimeSlot slot = new TimeSlot(
                dto.getStartTime(),
                dto.getEndTime(),
                dto.getName(),
                dto.getDescription()
        );

        TimeSlot savedSlot = timeSlotRepository.save(slot);
        log.info("Zaman slotu başarıyla oluşturuldu: id={}", savedSlot.getId());

        return convertToResponseDto(savedSlot);
    }

    public List<TimeSlotResponseDto> getAllActiveSlots() {
        List<TimeSlot> slots = timeSlotRepository.findAllActiveSlots();
        return convertToResponseDtoList(slots);
    }

    public TimeSlotResponseDto getSlotById(Long id) {
        TimeSlot slot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new TimeSlotNotFoundException("Zaman slotu bulunamadı: " + id));
        return convertToResponseDto(slot);
    }

    public void deleteSlot(Long id) {
        TimeSlot slot = timeSlotRepository.findById(id)
                .orElseThrow(() -> new TimeSlotNotFoundException("Zaman slotu bulunamadı: " + id));
        
        slot.setIsActive(false);
        timeSlotRepository.save(slot);
        log.info("Zaman slotu deaktif edildi: id={}", id);
    }

    public boolean isValidSlot(LocalTime startTime, LocalTime endTime) {
        Optional<TimeSlot> slot = timeSlotRepository.findActiveSlotByTime(startTime, endTime);
        return slot.isPresent();
    }

    public List<AvailableSlotDto> getAvailableSlotsForDate(LocalDate date, Long roomId) {
        List<TimeSlot> allSlots = timeSlotRepository.findAllActiveSlots();
        List<AvailableSlotDto> availableSlots = new ArrayList<>();

        for (TimeSlot slot : allSlots) {
            if (appointmentPeriodService.isAppointmentAllowedForDate(date, slot.getStartTime(), slot.getEndTime())) {
                boolean isOccupied = appointmentRepository.existsByRoomIdAndDateAndStartTimeAndEndTimeAndStatus(
                        roomId, date, slot.getStartTime(), slot.getEndTime(), Appointment.Status.ACTIVE
                );

                if (!isOccupied) {
                    AvailableSlotDto availableSlot = new AvailableSlotDto();
                    availableSlot.setSlotId(slot.getId());
                    availableSlot.setStartTime(slot.getStartTime());
                    availableSlot.setEndTime(slot.getEndTime());
                    availableSlot.setName(slot.getName());
                    availableSlot.setAvailable(true);
                    availableSlots.add(availableSlot);
                }
            }
        }

        return availableSlots;
    }

    private TimeSlotResponseDto convertToResponseDto(TimeSlot slot) {
        TimeSlotResponseDto dto = new TimeSlotResponseDto();
        dto.setId(slot.getId());
        dto.setStartTime(slot.getStartTime());
        dto.setEndTime(slot.getEndTime());
        dto.setName(slot.getName());
        dto.setDescription(slot.getDescription());
        dto.setIsActive(slot.getIsActive());
        return dto;
    }

    private List<TimeSlotResponseDto> convertToResponseDtoList(List<TimeSlot> slots) {
        List<TimeSlotResponseDto> dtoList = new ArrayList<>();
        for (TimeSlot slot : slots) {
            dtoList.add(convertToResponseDto(slot));
        }
        return dtoList;
    }
}
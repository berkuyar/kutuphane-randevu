package com.uyarberk.kutuphane_randevu.service;

import com.uyarberk.kutuphane_randevu.dto.AppointmentPeriodCreateRequestDto;
import com.uyarberk.kutuphane_randevu.dto.AppointmentPeriodResponseDto;
import com.uyarberk.kutuphane_randevu.exception.AppointmentPeriodNotFoundException;
import com.uyarberk.kutuphane_randevu.model.AppointmentPeriod;
import com.uyarberk.kutuphane_randevu.repository.AppointmentPeriodRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AppointmentPeriodService {

    private final AppointmentPeriodRepository appointmentPeriodRepository;

    public AppointmentPeriodService(AppointmentPeriodRepository appointmentPeriodRepository) {
        this.appointmentPeriodRepository = appointmentPeriodRepository;
    }

    public AppointmentPeriodResponseDto createPeriod(AppointmentPeriodCreateRequestDto dto) {
        log.info("Yeni randevu dönemi oluşturuluyor: başlangıç={}, bitiş={}", dto.getStartDate(), dto.getEndDate());

        if (dto.getEndDate().isBefore(dto.getStartDate())) {
            throw new IllegalArgumentException("Bitiş tarihi başlangıç tarihinden önce olamaz.");
        }

        if (dto.getAvailableEndTime().isBefore(dto.getAvailableStartTime()) || 
            dto.getAvailableEndTime().equals(dto.getAvailableStartTime())) {
            throw new IllegalArgumentException("Bitiş saati başlangıç saatinden sonra olmalıdır.");
        }

        AppointmentPeriod period = new AppointmentPeriod(
                dto.getStartDate(),
                dto.getEndDate(),
                dto.getAvailableStartTime(),
                dto.getAvailableEndTime(),
                dto.getDescription()
        );

        AppointmentPeriod savedPeriod = appointmentPeriodRepository.save(period);
        log.info("Randevu dönemi başarıyla oluşturuldu: id={}", savedPeriod.getId());

        return convertToResponseDto(savedPeriod);
    }

    public List<AppointmentPeriodResponseDto> getAllActivePeriods() {
        List<AppointmentPeriod> periods = appointmentPeriodRepository.findAllActivePeriods();
        return convertToResponseDtoList(periods);
    }

    public List<AppointmentPeriodResponseDto> getCurrentAndFuturePeriods() {
        List<AppointmentPeriod> periods = appointmentPeriodRepository.findCurrentAndFuturePeriods(LocalDate.now());
        return convertToResponseDtoList(periods);
    }

    public AppointmentPeriodResponseDto getPeriodById(Long id) {
        AppointmentPeriod period = appointmentPeriodRepository.findById(id)
                .orElseThrow(() -> new AppointmentPeriodNotFoundException("Randevu dönemi bulunamadı: " + id));
        return convertToResponseDto(period);
    }

    public void deletePeriod(Long id) {
        AppointmentPeriod period = appointmentPeriodRepository.findById(id)
                .orElseThrow(() -> new AppointmentPeriodNotFoundException("Randevu dönemi bulunamadı: " + id));
        
        period.setIsActive(false);
        appointmentPeriodRepository.save(period);
        log.info("Randevu dönemi deaktif edildi: id={}", id);
    }

    public boolean isAppointmentAllowedForDate(LocalDate date, LocalTime startTime, LocalTime endTime) {
        List<AppointmentPeriod> activePeriods = appointmentPeriodRepository.findActivePeriodsForDate(date);
        
        if (activePeriods.isEmpty()) {
            log.warn("Bu tarih için aktif randevu dönemi bulunamadı: {}", date);
            return false;
        }

        for (AppointmentPeriod period : activePeriods) {
            if (isTimeWithinPeriod(startTime, endTime, period.getAvailableStartTime(), period.getAvailableEndTime())) {
                return true;
            }
        }

        log.warn("Randevu saati izin verilen zaman aralığında değil: date={}, startTime={}, endTime={}", date, startTime, endTime);
        return false;
    }

    private boolean isTimeWithinPeriod(LocalTime appointmentStart, LocalTime appointmentEnd, 
                                     LocalTime periodStart, LocalTime periodEnd) {
        return (appointmentStart.equals(periodStart) || appointmentStart.isAfter(periodStart)) &&
               (appointmentEnd.equals(periodEnd) || appointmentEnd.isBefore(periodEnd));
    }

    private AppointmentPeriodResponseDto convertToResponseDto(AppointmentPeriod period) {
        AppointmentPeriodResponseDto dto = new AppointmentPeriodResponseDto();
        dto.setId(period.getId());
        dto.setStartDate(period.getStartDate());
        dto.setEndDate(period.getEndDate());
        dto.setAvailableStartTime(period.getAvailableStartTime());
        dto.setAvailableEndTime(period.getAvailableEndTime());
        dto.setIsActive(period.getIsActive());
        dto.setDescription(period.getDescription());
        return dto;
    }

    private List<AppointmentPeriodResponseDto> convertToResponseDtoList(List<AppointmentPeriod> periods) {
        List<AppointmentPeriodResponseDto> dtoList = new ArrayList<>();
        for (AppointmentPeriod period : periods) {
            dtoList.add(convertToResponseDto(period));
        }
        return dtoList;
    }
}
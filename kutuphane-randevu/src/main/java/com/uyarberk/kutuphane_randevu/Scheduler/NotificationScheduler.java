package com.uyarberk.kutuphane_randevu.Scheduler;

import com.uyarberk.kutuphane_randevu.model.Appointment;
import com.uyarberk.kutuphane_randevu.service.AppointmentService;
import com.uyarberk.kutuphane_randevu.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class NotificationScheduler {

    private final AppointmentService appointmentService;
    private final NotificationService notificationService;

    // Her gün sabah 8 de çalışır.
    @Scheduled(cron = "0 0 8 * * *")  // saniye dakika saat gün ay haftagünü
    public void notifyUpcomingAppointments() {
        LocalDate tomorrow = LocalDate.now().plusDays(1); // bugünün tarihini alıp 1 gün ekle.
        List<Appointment> appointments = appointmentService.getAllAppointmentsByDate(tomorrow);

        for(Appointment appointment : appointments){
            String message = "Yarın bir randevunuz var." +
                    appointment.getDate()+ " " + appointment.getStartTime() + " - " + appointment.getEndTime();
            notificationService.createNotification(message, appointment.getUser().getId());
        }
        log.info("Yarınki randevualar için bildirim gönderildi.", appointments.size());
    }
}

package com.uyarberk.kutuphane_randevu.repository;

import com.uyarberk.kutuphane_randevu.model.AppointmentPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AppointmentPeriodRepository extends JpaRepository<AppointmentPeriod, Long> {

    @Query("SELECT ap FROM AppointmentPeriod ap WHERE ap.isActive = true AND ap.startDate <= :date AND ap.endDate >= :date")
    List<AppointmentPeriod> findActivePeriodsForDate(@Param("date") LocalDate date);

    @Query("SELECT ap FROM AppointmentPeriod ap WHERE ap.isActive = true ORDER BY ap.startDate")
    List<AppointmentPeriod> findAllActivePeriods();

    @Query("SELECT ap FROM AppointmentPeriod ap WHERE ap.isActive = true AND ap.endDate >= :currentDate ORDER BY ap.startDate")
    List<AppointmentPeriod> findCurrentAndFuturePeriods(@Param("currentDate") LocalDate currentDate);
}
package com.uyarberk.kutuphane_randevu.repository;

import com.uyarberk.kutuphane_randevu.model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    @Query("SELECT ts FROM TimeSlot ts WHERE ts.isActive = true ORDER BY ts.startTime")
    List<TimeSlot> findAllActiveSlots();

    @Query("SELECT ts FROM TimeSlot ts WHERE ts.isActive = true AND ts.startTime = :startTime AND ts.endTime = :endTime")
    Optional<TimeSlot> findActiveSlotByTime(@Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);

    @Query("SELECT ts FROM TimeSlot ts WHERE ts.isActive = true AND ts.startTime <= :time AND ts.endTime > :time")
    List<TimeSlot> findSlotsContainingTime(@Param("time") LocalTime time);

    @Query("SELECT ts FROM TimeSlot ts WHERE ts.isActive = true AND " +
           "((ts.startTime <= :startTime AND ts.endTime > :startTime) OR " +
           "(ts.startTime < :endTime AND ts.endTime >= :endTime) OR " +
           "(ts.startTime >= :startTime AND ts.endTime <= :endTime))")
    List<TimeSlot> findOverlappingSlots(@Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime);
}
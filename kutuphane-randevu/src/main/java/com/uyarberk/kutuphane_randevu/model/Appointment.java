package com.uyarberk.kutuphane_randevu.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity // Bu sınıf bir veritabanı tablosunu temsil eder
@Table(name = "appointments") // Tablo adı appointments olacak
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID otomatik artar
    private Long id;

    // Randevuyu alan kullanıcı (bir kullanıcı birden fazla randevu alabilir)
    @ManyToOne
    @JoinColumn(name = "user_id") // Veritabanında foreign key olacak
    private User user;

    // Randevunun gerçekleşeceği oda
    @ManyToOne
    @JoinColumn(name = "room_id") // Veritabanında foreign key olacak
    private Room room;

    // Randevunun tarihi
    private LocalDate date;

    // Randevu başlangıç saati
    private LocalTime startTime;

    // Randevu bitiş saati
    private LocalTime endTime;

    // Randevunun durumu: ACTIVE veya CANCELED
    @Enumerated(EnumType.STRING) // Enum değerini string olarak sakla (örnek: "ACTIVE")
    private Status status;



    // Enum sınıfı: randevu durumu için
    public enum Status {
        ACTIVE,
        CANCELED
    }

    // ──────── Constructor (boş) ────────
    public Appointment() {
    }

    // ──────── Constructor (dolu) ────────
    public Appointment(Long id, User user, Room room, LocalDate date, LocalTime startTime, LocalTime endTime, Status status) {
        this.id = id;
        this.user = user;
        this.room = room;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    // ──────── Getter'lar ────────

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Room getRoom() {
        return room;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public Status getStatus() {
        return status;
    }

    // ──────── Setter'lar ────────

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}

package com.uyarberk.kutuphane_randevu.model;

import jakarta.persistence.*;

@Entity
@Table(name = "rooms")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Integer capacity;

    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;

    // ─── GETTER ───────────────────────────────────────────────────────────────

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    // ─── SETTER ───────────────────────────────────────────────────────────────

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    // ─── CONSTRUCTORS ────────────────────────────────────────────────────────

    public Room() {
    }

    public Room(Long id, String name, String description, Integer capacity, User createdBy) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.capacity = capacity;
        this.createdBy = createdBy;
    }
}

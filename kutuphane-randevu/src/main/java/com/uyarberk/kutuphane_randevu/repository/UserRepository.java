package com.uyarberk.kutuphane_randevu.repository;

import com.uyarberk.kutuphane_randevu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}

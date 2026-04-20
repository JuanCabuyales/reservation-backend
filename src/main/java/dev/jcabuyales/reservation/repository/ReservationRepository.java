package dev.jcabuyales.reservation.repository;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.jcabuyales.reservation.entidades.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByDateAndTime(LocalDate date, LocalTime time);
}
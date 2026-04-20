package dev.jcabuyales.reservation.service;

import dev.jcabuyales.reservation.entidades.Reservation;
import dev.jcabuyales.reservation.entidades.ReservationStatus;
import dev.jcabuyales.reservation.exception.BusinessRuleException;
import dev.jcabuyales.reservation.repository.ReservationRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Transactional(readOnly = true)
    public List<Reservation> listarReservas() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation crearReserva(Reservation request) {
        if (reservationRepository.existsByDateAndTime(request.getDate(), request.getTime())) {
            throw new BusinessRuleException("Ya existe una reserva activa para la fecha y hora indicadas.");
        }
        Reservation reservation = new Reservation();
        reservation.setClientName(request.getClientName());
        reservation.setDate(request.getDate());
        reservation.setTime(request.getTime());
        reservation.setService(request.getService());
        reservation.setStatus(ReservationStatus.ACTIVE);
        return reservationRepository.save(reservation);
    }

    @Transactional
    public void cancelarReserva(Long id) {
        Reservation reservation =
                reservationRepository
                        .findById(id)
                        .orElseThrow(
                                () ->
                                        new BusinessRuleException(
                                                "No existe la reserva con id: " + id));
        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new BusinessRuleException("La reserva ya está cancelada.");
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);
    }
}

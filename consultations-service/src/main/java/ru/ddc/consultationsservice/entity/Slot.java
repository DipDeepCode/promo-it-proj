package ru.ddc.consultationsservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import ru.ddc.consultationsservice.exception.ApiException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "slots")
public class Slot {

    public enum Status {OPEN, CLOSED}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "specialist_id", nullable = false, updatable = false)
    private UUID specialistId;

    @Column(name = "begin_at", nullable = false, updatable = false)
    private LocalDateTime beginAt;

    @Column(name = "end_at", nullable = false, updatable = false)
    private LocalDateTime endAt;

    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    private UUID createdBy;

    @OneToMany(mappedBy = "slot", orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    public void addReservation(Reservation reservation) {

        final UUID clientId = reservation.getClientId();
        if (this.reservations
                .stream()
                .anyMatch(element -> element.getClientId().equals(clientId))) {
            throw new ApiException("The client has already booked an reservation for that time slot");
        }

        this.reservations.add(reservation);
        reservation.setSlot(this);
    }

    public void removeReservation(Reservation reservation) {
        this.reservations.remove(reservation);
        reservation.setSlot(null);
    }
}

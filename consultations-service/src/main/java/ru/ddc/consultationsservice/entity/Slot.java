package ru.ddc.consultationsservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;
import ru.ddc.consultationsservice.exception.ApiException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "slots")
@Comment("Stores information about slots")
public class Slot {

    public enum Status {OPEN, CLOSED}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Comment(value = "Primary key")
    private Long id;

    @Column(name = "specialist_id", nullable = false, updatable = false)
    @Comment("UUID of the specialist who will conduct the consultation in this slot")
    private UUID specialistId;

    @Column(name = "begin_at", nullable = false, updatable = false)
    @Comment("Date and time of the start of the slot")
    private LocalDateTime beginAt;

    @Column(name = "end_at", nullable = false, updatable = false)
    @Comment("Date and time of the end of the slot")
    private LocalDateTime endAt;

    @Column(name = "status", nullable = false)
    @Comment("The status of the slot. Possible values are OPEN, CLOSED")
    private Status status;

    @Column(name = "cost", nullable = false)
    @Comment("The cost of the consultation in this slot")
    private BigDecimal cost;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("Date and time of slot creation")
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    @Comment("UUID of the user who created the slot")
    private UUID createdBy;

    @OneToMany(mappedBy = "slot", orphanRemoval = true)
    @Comment("List of reservations submitted for this slot")
    private List<Reservation> reservations = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "slot_context_entries")
    @MapKeyColumn(name = "name")
    @Column(name = "description")
    @Comment(value = "Description of the context entry")
    private Map<String, String> context = new HashMap<>();

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

package ru.ddc.consultationsservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "reservations",
        uniqueConstraints = @UniqueConstraint(
                name = "slot_client_unique",
                columnNames = {"slot_id", "client_id"}))
@Comment("Stores information about reservations")
public class Reservation {

    public enum Status {NEW, APPROVED, REJECTED}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Comment("Primary key")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "slot_id", nullable = false, updatable = false)
    @Comment("Foreign key to slot")
    private Slot slot;

    @Column(name = "client_id", nullable = false, updatable = false)
    @Comment("UUID of client who submitted the application")
    private UUID clientId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    @Comment("The status of the reservation. Possible values are NEW, APPROVED, REJECTED")
    private Status status;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Comment("Date and time of reservation creation")
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    @Comment("UUID of the user who created the reservation")
    private UUID createdBy;
}

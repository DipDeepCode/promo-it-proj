package ru.ddc.consultationsservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "reservations",
        uniqueConstraints = @UniqueConstraint(
                name = "slot_client_unique",
                columnNames = {"slot_id", "client_id"}))
public class Reservation {

    public enum Status {NEW, APPROVED, REJECTED}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "slot_id", nullable = false, updatable = false)
    private Slot slot;

    @Column(name = "client_id", nullable = false, updatable = false)
    private UUID clientId;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "created_by", nullable = false, updatable = false)
    private UUID createdBy;
}

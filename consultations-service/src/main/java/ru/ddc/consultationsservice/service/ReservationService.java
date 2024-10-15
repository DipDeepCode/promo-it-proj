package ru.ddc.consultationsservice.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.ddc.consultationsservice.controller.payload.ReservationDto;
import ru.ddc.consultationsservice.controller.payload.CreateReservationRequest;
import ru.ddc.consultationsservice.controller.payload.UpdateReservationRequest;
import ru.ddc.consultationsservice.entity.Reservation;
import ru.ddc.consultationsservice.entity.Slot;
import ru.ddc.consultationsservice.exception.ApiException;
import ru.ddc.consultationsservice.repository.ReservationRepository;
import ru.ddc.consultationsservice.rules.EntityRulesService;
import ru.ddc.consultationsservice.rules.RuleResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import static ru.ddc.consultationsservice.entity.Reservation.Status.*;

@Service
@Validated
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final SlotService slotService;
    private final ModelMapper modelMapper;
    private final EntityRulesService<Reservation, RuleResponse> rulesService;
    private final EntityManager entityManager;

    @Transactional
    public ReservationDto create(@NotNull final Long slotId, @Valid CreateReservationRequest request) {
        final Reservation reservation = modelMapper.map(request, Reservation.class);
        Slot slot = slotService.getSlot(slotId);
        reservation.setSlot(slot);
        reservation.setStatus(NEW);
        reservation.setCreatedAt(LocalDateTime.now());
        reservation.setCreatedBy(getCurrentUserUUID());
        applyCanRule(() -> rulesService.canCreate(reservation), () -> {
            slot.addReservation(reservation);
            reservationRepository.save(reservation);
        });
        return modelMapper.map(reservation, ReservationDto.class);
    }

    @Transactional(readOnly = true)
    public List<ReservationDto> getAll() {
        return reservationRepository
                .findAll()
                .stream()
                .map((reservation) -> modelMapper.map(reservation, ReservationDto.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReservationDto> getBySlotId(Long slotId) {
        return reservationRepository
                .findBySlot_Id(slotId)
                .stream()
                .map((reservation) -> modelMapper.map(reservation, ReservationDto.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public ReservationDto getById(@NotNull Long id) {
        Reservation reservation = getReservation(id);
        return modelMapper.map(reservation, ReservationDto.class);
    }

    @Transactional
    public ReservationDto update(@NotNull Long id, @Valid final UpdateReservationRequest request) {
        final Reservation reservation = getReservation(id);
        entityManager.detach(reservation);
        modelMapper.map(request, reservation);
        final Reservation reservation1 = getReservation(id);
        applyCanRule(() -> rulesService.canUpdate(reservation), () -> {
            modelMapper.map(request, reservation1);
            handleOtherReservationsInSlot(reservation1);
        });
        return modelMapper.map(reservation1, ReservationDto.class);
    }

    @Transactional
    public void delete(final Long id) {
        final Reservation reservation = getReservation(id);
        applyCanRule(() -> rulesService.canDelete(reservation), () -> {
            reservation.getSlot().removeReservation(reservation);
            reservationRepository.deleteById(id);
        });
    }

    private Reservation getReservation(Long id) {
        return reservationRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Reservation with id=%d not found", id)));
    }

    private void handleOtherReservationsInSlot(final Reservation reservation) { // TODO try to move to rules
        if (reservation.getStatus() == APPROVED) {
            Long slotId = reservation.getSlot().getId();
            reservationRepository.findBySlot_Id(slotId)
                    .stream()
                    .filter(reservation1 -> !reservation1.equals(reservation))
                    .forEach(reservation1 -> reservation1.setStatus(REJECTED));
        }
    }

    private void applyCanRule(Supplier<RuleResponse> supplier, Runnable runnable) {
        RuleResponse response = supplier.get();
        if (response.isAllowed()) {
            runnable.run();
        } else {
            throw new ApiException("The operation is not applicable", response.getReason());
        }
    }

    private UUID getCurrentUserUUID() {
        JwtAuthenticationToken authenticationToken =
                (JwtAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        Jwt jwt = (Jwt) authenticationToken.getCredentials();
        return UUID.fromString(jwt.getClaim("sub"));
    }
}

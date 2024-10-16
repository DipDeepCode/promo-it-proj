package ru.ddc.consultationsservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.ddc.consultationsservice.controller.payload.ReservationDto;
import ru.ddc.consultationsservice.controller.payload.CreateReservationRequest;
import ru.ddc.consultationsservice.controller.payload.UpdateReservationRequest;
import ru.ddc.consultationsservice.service.ReservationService;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_MODERATOR')")
    @PostMapping("slots/{slotId}/reservations")
    public ResponseEntity<?> create(@PathVariable Long slotId, @RequestBody CreateReservationRequest request) {
        ReservationDto reservationDto = reservationService.create(slotId, request);
        reservationDto.add(linkTo(methodOn(ReservationController.class).create(slotId, request)).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationDto);
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @GetMapping("/reservations")
    public ResponseEntity<?> getAll() {
        List<ReservationDto> reservationDtoList = reservationService.getAll();
        CollectionModel<ReservationDto> collectionModel = CollectionModel.of(reservationDtoList
                .stream()
                .peek(reservationDto -> reservationDto.add(linkTo(methodOn(ReservationController.class)
                        .getById(reservationDto.getId())).withRel("reservations")))
                .toList());
        collectionModel.add(linkTo(methodOn(ReservationController.class).getAll()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @PreAuthorize("hasAnyRole('ROLE_SPECIALIST', 'ROLE_MODERATOR')")
    @GetMapping("/slots/{slotId}/reservations")
    public ResponseEntity<?> getSlotReservation(@PathVariable Long slotId) {
        List<ReservationDto> reservationDtoList = reservationService.getBySlotId(slotId);
        CollectionModel<ReservationDto> collectionModel = CollectionModel.of(reservationDtoList
                .stream()
                .peek(reservationDto -> reservationDto.add(linkTo(methodOn(ReservationController.class)
                        .getById(reservationDto.getId())).withRel("reservations")))
                .toList());
        collectionModel.add(linkTo(methodOn(ReservationController.class).getSlotReservation(slotId)).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_SPECIALIST', 'ROLE_MODERATOR')")
    @GetMapping("/reservations/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        ReservationDto reservationDto = reservationService.getById(id);
        reservationDto.add(linkTo(methodOn(ReservationController.class).getById(id)).withSelfRel());
        return ResponseEntity.ok(reservationDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_SPECIALIST', 'ROLE_MODERATOR')")
    @PutMapping("reservations/{id}")
    public ResponseEntity<ReservationDto> update(@PathVariable Long id,
                                                 @RequestBody UpdateReservationRequest request) {
        ReservationDto reservationDto = reservationService.update(id, request);
        reservationDto.add(linkTo(methodOn(ReservationController.class).getById(id)).withSelfRel());
        return ResponseEntity.ok(reservationDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/reservations/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

package ru.ddc.consultationsservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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

@Tag(name = "ReservationController", description = "REST controller for Reservations")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @Operation(summary = "Create Reservation")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Reservation created",
                    content = @Content(schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Permission denied",
                    content = @Content())
    })
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_MODERATOR')")
    @PostMapping("slots/{slotId}/reservations")
    public ResponseEntity<?> create(
            @Parameter(description = "Slot Id") @PathVariable Long slotId,
            @ParameterObject @RequestBody CreateReservationRequest request) {
        ReservationDto reservationDto = reservationService.create(slotId, request);
        reservationDto.add(linkTo(methodOn(ReservationController.class).create(slotId, request)).withSelfRel());
        return ResponseEntity.status(HttpStatus.CREATED).body(reservationDto);
    }

    @Operation(summary = "Get all Reservations")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Request successful",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReservationDto.class)))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Permission denied",
                    content = @Content())
    })
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

    @Operation(summary = "Get all Reservations for Slot")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Request successful",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReservationDto.class)))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Permission denied",
                    content = @Content())
    })
    @PreAuthorize("hasAnyRole('ROLE_SPECIALIST', 'ROLE_MODERATOR')")
    @GetMapping("/slots/{slotId}/reservations")
    public ResponseEntity<?> getSlotReservations(@Parameter(description = "Slot Id") @PathVariable Long slotId) {
        List<ReservationDto> reservationDtoList = reservationService.getBySlotId(slotId);
        CollectionModel<ReservationDto> collectionModel = CollectionModel.of(reservationDtoList
                .stream()
                .peek(reservationDto -> reservationDto.add(linkTo(methodOn(ReservationController.class)
                        .getById(reservationDto.getId())).withRel("reservations")))
                .toList());
        collectionModel.add(linkTo(methodOn(ReservationController.class).getSlotReservations(slotId)).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Get Reservation by Id")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Request successful",
                    content = @Content(schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Permission denied",
                    content = @Content())
    })
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_SPECIALIST', 'ROLE_MODERATOR')")
    @GetMapping("/reservations/{id}")
    public ResponseEntity<?> getById(@Parameter(description = "Reservation Id") @PathVariable Long id) {
        ReservationDto reservationDto = reservationService.getById(id);
        reservationDto.add(linkTo(methodOn(ReservationController.class).getById(id)).withSelfRel());
        return ResponseEntity.ok(reservationDto);
    }

    @Operation(summary = "Update Reservation")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Update successful",
                    content = @Content(schema = @Schema(implementation = ReservationDto.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Permission denied",
                    content = @Content())
    })
    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_SPECIALIST', 'ROLE_MODERATOR')")
    @PutMapping("reservations/{id}")
    public ResponseEntity<ReservationDto> update(
            @Parameter(description = "Reservation Id") @PathVariable Long id,
            @ParameterObject @RequestBody UpdateReservationRequest request) {
        ReservationDto reservationDto = reservationService.update(id, request);
        reservationDto.add(linkTo(methodOn(ReservationController.class).getById(id)).withSelfRel());
        return ResponseEntity.ok(reservationDto);
    }

    @Operation(summary = "Delete Reservation")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Delete successful",
                    content = @Content()),
            @ApiResponse(
                    responseCode = "401",
                    description = "Permission denied",
                    content = @Content())
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/reservations/{id}")
    public ResponseEntity<?> delete(@Parameter(description = "Reservation Id") @PathVariable Long id) {
        reservationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

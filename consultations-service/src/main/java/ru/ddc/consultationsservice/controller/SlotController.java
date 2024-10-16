package ru.ddc.consultationsservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.ddc.consultationsservice.controller.payload.CreateSlotRequest;
import ru.ddc.consultationsservice.controller.payload.SlotCriteria;
import ru.ddc.consultationsservice.controller.payload.SlotDto;
import ru.ddc.consultationsservice.controller.payload.UpdateSlotRequest;
import ru.ddc.consultationsservice.service.SlotService;

import java.util.List;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Tag(name = "SlotController", description = "REST controller for Slot")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SlotController {
    private final SlotService slotService;

    @Operation(summary = "Create Slot")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Slot created",
                    content = @Content(schema = @Schema(implementation = SlotDto.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Permission denied",
                    content = @Content())
    })
    @PreAuthorize("hasAnyRole('ROLE_SPECIALIST', 'ROLE_MODERATOR')")
    @PostMapping(value = "/slots", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(
            @Parameter(description = "Request to create a slot")
            @RequestBody CreateSlotRequest request) {
        SlotDto slotDto = slotService.create(request);
        slotDto.add(linkTo(methodOn(SlotController.class).getById(slotDto.getId())).withSelfRel());
        slotDto.add(linkTo(methodOn(ReservationController.class).getSlotReservation(slotDto.getId())).withRel("reservations"));
        return ResponseEntity.status(HttpStatus.CREATED).body(slotDto);
    }

    @PreAuthorize("hasRole('ROLE_MODERATOR')")
    @GetMapping(value = "/slots", produces = "application/hal+json")
    public ResponseEntity<?> getAll(SlotCriteria criteria) {
        List<SlotDto> slotDtoList = slotService.getAll(criteria);
        CollectionModel<SlotDto> collectionModel = CollectionModel.of(slotDtoList
                .stream()
                .peek(slotDto -> slotDto.add(linkTo(methodOn(SlotController.class).getById(slotDto.getId())).withRel("slot")))
                .peek(slotDto -> slotDto.add(linkTo(methodOn(ReservationController.class).getSlotReservation(slotDto.getId())).withRel("reservations")))
                .toList());
        collectionModel.add(linkTo(methodOn(SlotController.class).getAll(criteria)).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_SPECIALIST', 'ROLE_MODERATOR')")
    @GetMapping(value = "/specialist/{id}/slots", produces = "application/hal+json")
    public ResponseEntity<?> getSpecialistSlots(@PathVariable UUID id,
                                                SlotCriteria criteria) {
        List<SlotDto> slotDtoList = slotService.getSpecialistSlots(id, criteria);
        CollectionModel<SlotDto> collectionModel = CollectionModel.of(slotDtoList
                .stream()
                .peek(slotDto -> slotDto.add(linkTo(methodOn(SlotController.class).getById(slotDto.getId())).withRel("slot")))
                .peek(slotDto -> slotDto.add(linkTo(methodOn(ReservationController.class).getSlotReservation(slotDto.getId())).withRel("reservations")))
                .toList());
        collectionModel.add(linkTo(methodOn(SlotController.class).getSpecialistSlots(id, criteria)).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @PreAuthorize("hasAnyRole('ROLE_CUSTOMER', 'ROLE_SPECIALIST', 'ROLE_MODERATOR')")
    @GetMapping(value = "/slots/{id}", produces = "application/hal+json")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        SlotDto slotDto = slotService.getById(id);
        slotDto.add(linkTo(methodOn(SlotController.class).getById(slotDto.getId())).withSelfRel());
        slotDto.add(linkTo(methodOn(ReservationController.class).getSlotReservation(slotDto.getId())).withRel("reservations"));
        return ResponseEntity.ok(slotDto);
    }

    @PreAuthorize("hasAnyRole('ROLE_SPECIALIST', 'ROLE_MODERATOR')")
    @PutMapping(value = "/slots/{id}", produces = "application/hal+json")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody UpdateSlotRequest request) {
        SlotDto slotDto = slotService.update(id, request);
        slotDto.add(linkTo(methodOn(SlotController.class).getById(slotDto.getId())).withSelfRel());
        slotDto.add(linkTo(methodOn(ReservationController.class).getSlotReservation(slotDto.getId())).withRel("reservations"));
        return ResponseEntity.ok(slotDto);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/slots/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        slotService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

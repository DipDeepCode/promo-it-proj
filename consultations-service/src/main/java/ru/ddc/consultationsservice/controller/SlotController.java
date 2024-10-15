package ru.ddc.consultationsservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class SlotController {
    private final SlotService slotService;

    @PostMapping(value = "/slots", produces = "application/hal+json")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@RequestBody CreateSlotRequest request) {
        SlotDto slotDto = slotService.create(request);
        slotDto.add(linkTo(methodOn(SlotController.class).getById(slotDto.getId())).withSelfRel());
        slotDto.add(linkTo(methodOn(ReservationController.class).getSlotReservation(slotDto.getId())).withRel("reservations"));
        return ResponseEntity.status(HttpStatus.CREATED).body(slotDto);
    }

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

    @GetMapping(value = "/slots/{id}", produces = "application/hal+json")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        SlotDto slotDto = slotService.getById(id);
        slotDto.add(linkTo(methodOn(SlotController.class).getById(slotDto.getId())).withSelfRel());
        slotDto.add(linkTo(methodOn(ReservationController.class).getSlotReservation(slotDto.getId())).withRel("reservations"));
        return ResponseEntity.ok(slotDto);
    }

    @PutMapping(value = "/slots/{id}", produces = "application/hal+json")
    public ResponseEntity<?> update(@PathVariable Long id,
                                    @RequestBody UpdateSlotRequest request) {
        SlotDto slotDto = slotService.update(id, request);
        slotDto.add(linkTo(methodOn(SlotController.class).getById(slotDto.getId())).withSelfRel());
        slotDto.add(linkTo(methodOn(ReservationController.class).getSlotReservation(slotDto.getId())).withRel("reservations"));
        return ResponseEntity.ok(slotDto);
    }

    @DeleteMapping("/slots/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        slotService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

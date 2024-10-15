package ru.ddc.consultationsservice.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.ddc.consultationsservice.controller.payload.CreateSlotRequest;
import ru.ddc.consultationsservice.controller.payload.SlotCriteria;
import ru.ddc.consultationsservice.controller.payload.SlotDto;
import ru.ddc.consultationsservice.controller.payload.UpdateSlotRequest;
import ru.ddc.consultationsservice.entity.Slot;
import ru.ddc.consultationsservice.exception.ApiException;
import ru.ddc.consultationsservice.repository.SlotRepository;
import ru.ddc.consultationsservice.rules.EntityRulesService;
import ru.ddc.consultationsservice.rules.RuleResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@Service
@Validated
@RequiredArgsConstructor
public class SlotService {
    private final SlotRepository slotRepository;
    private final ModelMapper modelMapper;
    private final EntityRulesService<Slot, RuleResponse> rulesService;

    @Transactional
    public SlotDto create(@Valid CreateSlotRequest request) {
        Slot slot = modelMapper.map(request, Slot.class);
        slot.setStatus(Slot.Status.OPEN);
        slot.setCreatedAt(LocalDateTime.now());
        slot.setCreatedBy(getCurrentUserUUID());
        applyCanRule(() -> rulesService.canCreate(slot), () -> slotRepository.save(slot));
        return modelMapper.map(slot, SlotDto.class);
    }

    @Transactional(readOnly = true)
    public List<SlotDto> getAll(SlotCriteria criteria) {
        return slotRepository
                .findAll(criteria.getSpecification())
                .stream()
                .map(slot -> modelMapper.map(slot, SlotDto.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SlotDto> getSpecialistSlots(@NotNull UUID specialistId,
                                            SlotCriteria criteria) {

        Specification<Slot> spec1 = (slot, cq, cb) -> cb.equal(slot.get("specialistId"), specialistId);
        Specification<Slot> spec2 = criteria.getSpecification();

        return slotRepository
                .findAll(spec1.and(spec2))
                .stream()
                .map((element) -> modelMapper.map(element, SlotDto.class))
                .toList();
    }

    @Transactional(readOnly = true)
    public SlotDto getById(@NotNull Long id) {
        Slot slot = getSlot(id);
        return modelMapper.map(slot, SlotDto.class);
    }

    @Transactional
    public SlotDto update(@NotNull Long id, @Valid final UpdateSlotRequest request) {
        final Slot slot = getSlot(id);
        applyCanRule(() -> rulesService.canUpdate(slot), () -> modelMapper.map(request, slot));
        return modelMapper.map(slot, SlotDto.class);
    }

    @Transactional
    public void delete(final Long id) {
        final Slot slot = getSlot(id);
        applyCanRule(() -> rulesService.canDelete(slot), () -> slotRepository.deleteById(id));
    }

    protected Slot getSlot(Long id) {
        return slotRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Slot with id=%d not found", id)));
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

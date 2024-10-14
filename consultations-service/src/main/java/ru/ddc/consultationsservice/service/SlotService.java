package ru.ddc.consultationsservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import ru.ddc.consultationsservice.controller.payload.CreateSlotRequest;
import ru.ddc.consultationsservice.controller.payload.SlotDto;
import ru.ddc.consultationsservice.entity.Slot;
import ru.ddc.consultationsservice.repository.SlotRepository;

import java.time.LocalDateTime;

import static ru.ddc.consultationsservice.util.AuthorizationUtil.getCurrentUserSubjectId;

@Service
@Validated
@RequiredArgsConstructor
public class SlotService {
    private final SlotRepository slotRepository;
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;

    @Transactional
    public SlotDto create(@Valid CreateSlotRequest request) {
        System.out.println(request);
        Slot slot = modelMapper.map(request, Slot.class);
//        slot.setCreatedBy(getCurrentUserSubjectId());
//        slot.setCreatedAt(LocalDateTime.now());
//        System.out.println(slot);
//        slot = slotRepository.save(slot);
        return null;//modelMapper.map(slot, SlotDto.class);
    }
}

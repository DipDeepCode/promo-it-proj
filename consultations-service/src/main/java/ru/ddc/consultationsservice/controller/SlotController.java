package ru.ddc.consultationsservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.ddc.consultationsservice.controller.payload.CreateSlotRequest;
import ru.ddc.consultationsservice.controller.payload.SlotDto;
import ru.ddc.consultationsservice.service.SlotService;

@RestController
@RequestMapping("/api/v1/slots")
@RequiredArgsConstructor
public class SlotController {
    private final SlotService slotService;

    @PostMapping
    public SlotDto create(@RequestBody CreateSlotRequest request) {
        return slotService.create(request);
    }
}

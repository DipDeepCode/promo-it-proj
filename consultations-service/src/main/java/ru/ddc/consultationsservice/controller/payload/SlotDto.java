package ru.ddc.consultationsservice.controller.payload;

import java.time.LocalDateTime;
import java.util.UUID;

public class SlotDto {
    private Long id;
    private UUID specialistId;
    private LocalDateTime beginAt;
    private LocalDateTime endAt;
    private LocalDateTime createdAt;
    private UUID createdBy;
}

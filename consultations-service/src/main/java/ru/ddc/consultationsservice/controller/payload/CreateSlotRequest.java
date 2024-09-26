package ru.ddc.consultationsservice.controller.payload;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CreateSlotRequest {

    @NotNull
    private UUID specialistId;

    @NotNull
    @Future
    private LocalDateTime beginAt;

    @NotNull
    @Future
    private LocalDateTime endAt;
}

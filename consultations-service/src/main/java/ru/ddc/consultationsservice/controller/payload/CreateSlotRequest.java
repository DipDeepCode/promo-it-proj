package ru.ddc.consultationsservice.controller.payload;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.ddc.consultationsservice.validation.DateTwoIsAfterDateOne;
import ru.ddc.consultationsservice.validation.SlotsDatesNotOverlap;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@DateTwoIsAfterDateOne(dateOne = "beginAt", dateTwo = "endAt")
@SlotsDatesNotOverlap(specialistId = "specialistId", beginDate = "beginAt", endDate = "endAt")
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

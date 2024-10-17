package ru.ddc.consultationsservice.controller.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.ddc.consultationsservice.validation.DateTwoIsAfterDateOne;
import ru.ddc.consultationsservice.validation.SlotsDatesNotOverlap;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Data
@DateTwoIsAfterDateOne(dateOne = "beginAt", dateTwo = "endAt")
@SlotsDatesNotOverlap(specialistId = "specialistId", beginDate = "beginAt", endDate = "endAt")
public class CreateSlotRequest {

    @Schema(description = "UUID of the specialist who will conduct the consultation")
    @NotNull
    private UUID specialistId;

    @Schema(description = "Date and time of the start of the slot")
    @NotNull
    @Future
    private LocalDateTime beginAt;

    @Schema(description = "Date and time of the end of the slot")
    @NotNull
    @Future
    private LocalDateTime endAt;

    @Schema(description = "The cost of the consultation in this slot")
    @NotNull
    private BigDecimal cost;

    @Schema(description = "Additional slot context")
    private Map<String, String> context;
}

package ru.ddc.consultationsservice.controller.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.ddc.consultationsservice.validation.EnumNamePattern;

@Data
public class UpdateSlotRequest {

    @Schema(description = "Status of the Slot")
    @EnumNamePattern(regexp = "OPEN|CLOSED")
    private String status;
}

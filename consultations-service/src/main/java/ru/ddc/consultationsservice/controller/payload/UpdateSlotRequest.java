package ru.ddc.consultationsservice.controller.payload;

import lombok.Data;
import ru.ddc.consultationsservice.validation.EnumNamePattern;

@Data
public class UpdateSlotRequest {

    @EnumNamePattern(regexp = "OPEN|CLOSED")
    private String status;
}

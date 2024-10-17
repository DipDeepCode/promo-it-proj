package ru.ddc.consultationsservice.controller.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import ru.ddc.consultationsservice.entity.Reservation;
import ru.ddc.consultationsservice.validation.EnumNamePattern;

@Data
public class UpdateReservationRequest {

    @Schema(description = "Status of the Reservation")
    @EnumNamePattern(regexp = "NEW|APPROVED|REJECTED")
    private Reservation.Status status;
}

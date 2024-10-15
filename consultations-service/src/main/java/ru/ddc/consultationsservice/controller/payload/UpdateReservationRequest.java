package ru.ddc.consultationsservice.controller.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.ddc.consultationsservice.entity.Reservation;

@Data
public class UpdateReservationRequest {

    @NotNull
    private Reservation.Status status;
}

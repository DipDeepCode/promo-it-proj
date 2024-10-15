package ru.ddc.consultationsservice.controller.payload;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateReservationRequest {

    @NotNull
    private UUID clientId;
}

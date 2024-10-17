package ru.ddc.consultationsservice.controller.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateReservationRequest {

    @Schema(description = "UUID of client who submitted the application")
    @NotNull
    private UUID clientId;
}

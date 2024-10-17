package ru.ddc.consultationsservice.controller.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto extends RepresentationModel<ReservationDto> {
    private Long id;
    private UUID clientId;
    private String status;
    private LocalDateTime createdAt;
    private UUID createdBy;
}

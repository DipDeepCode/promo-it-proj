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
public class SlotDto extends RepresentationModel<SlotDto> {
    private Long id;
    private UUID specialistId;
    private LocalDateTime beginAt;
    private LocalDateTime endAt;
    private String status;
    private LocalDateTime createdAt;
    private UUID createdBy;
}

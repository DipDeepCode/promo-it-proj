package ru.ddc.consultationsservice.controller.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
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
    private BigDecimal cost;
    private LocalDateTime createdAt;
    private UUID createdBy;
    private Map<String, String> context;
}

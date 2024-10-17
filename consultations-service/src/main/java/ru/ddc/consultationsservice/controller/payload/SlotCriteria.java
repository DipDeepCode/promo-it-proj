package ru.ddc.consultationsservice.controller.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import ru.ddc.consultationsservice.entity.Slot;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SlotCriteria implements Criteria<Slot> {

    @Schema(description = "The date for which you need to get the slots")
    private LocalDate date;

    @Schema(description = "The start date of the period for which you need to get slots")
    private LocalDate startDate;

    @Schema(description = "The end date of the period for which you need to get slots")
    private LocalDate endDate;

    @Override
    public Specification<Slot> getSpecification() {
        if (date != null) {
            return (slot, cq, cb) -> cb.between(slot.get("beginAt"), date.atStartOfDay(), date.atTime(LocalTime.MAX));
        } else if (startDate != null && endDate != null) {
            return (slot, cq, cb) -> cb.between(slot.get("beginAt"), startDate.atStartOfDay(), endDate.atTime(LocalTime.MAX));
        } else {
            return (slot, cq, cb) -> cb.conjunction();
        }
    }
}

package ru.ddc.consultationsservice.controller.payload;

import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import ru.ddc.consultationsservice.entity.Slot;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class SlotCriteria implements Criteria<Slot> {
    private LocalDate date;
    private LocalDate date1;
    private LocalDate date2;

    @Override
    public Specification<Slot> getSpecification() {
        if (date != null) {
            return (slot, cq, cb) -> cb.between(slot.get("beginAt"), date.atStartOfDay(), date.atTime(LocalTime.MAX));
        } else if (date1 != null && date2 != null) {
            return (slot, cq, cb) -> cb.between(slot.get("beginAt"), date1.atStartOfDay(), date2.atTime(LocalTime.MAX));
        } else {
            return (slot, cq, cb) -> cb.conjunction();
        }
    }
}

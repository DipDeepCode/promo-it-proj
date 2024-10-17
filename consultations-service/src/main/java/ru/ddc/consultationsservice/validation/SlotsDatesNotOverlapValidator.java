package ru.ddc.consultationsservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;
import ru.ddc.consultationsservice.repository.SlotRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SlotsDatesNotOverlapValidator implements ConstraintValidator<SlotsDatesNotOverlap, Object> {
    private final SlotRepository slotRepository;
    private String beginAtFieldName;
    private String endAtFieldName;
    private String specialistIdFieldName;

    @Override
    public void initialize(SlotsDatesNotOverlap constraintAnnotation) {
        this.specialistIdFieldName = constraintAnnotation.specialistId();
        this.beginAtFieldName = constraintAnnotation.beginDate();
        this.endAtFieldName = constraintAnnotation.endDate();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        UUID specialistId = (UUID) new BeanWrapperImpl(value).getPropertyValue(specialistIdFieldName);
        LocalDateTime beginAt = (LocalDateTime) new BeanWrapperImpl(value).getPropertyValue(beginAtFieldName);
        LocalDateTime endAt = (LocalDateTime) new BeanWrapperImpl(value).getPropertyValue(endAtFieldName);

        return !slotRepository.isDatesOverlaps(specialistId, beginAt, endAt);
    }
}

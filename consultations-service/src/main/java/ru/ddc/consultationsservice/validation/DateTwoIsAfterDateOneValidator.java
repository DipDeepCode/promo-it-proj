package ru.ddc.consultationsservice.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.time.LocalDateTime;

public class DateTwoIsAfterDateOneValidator implements ConstraintValidator<DateTwoIsAfterDateOne, Object> {
    private String dateOneFieldName;
    private String dateTwoFieldName;

    @Override
    public void initialize(DateTwoIsAfterDateOne constraintAnnotation) {
        this.dateOneFieldName = constraintAnnotation.dateOne();
        this.dateTwoFieldName = constraintAnnotation.dateTwo();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        LocalDateTime dateOneValue = (LocalDateTime) new BeanWrapperImpl(value).getPropertyValue(dateOneFieldName);
        LocalDateTime dateTwoValue = (LocalDateTime) new BeanWrapperImpl(value).getPropertyValue(dateTwoFieldName);
        return dateOneValue == null || dateTwoValue == null || dateTwoValue.isAfter(dateOneValue);
    }
}

package ru.ddc.consultationsservice.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = DateTwoIsAfterDateOneValidator.class)
@Target(TYPE)
@Retention(RUNTIME)
public @interface DateTwoIsAfterDateOne {
    String message() default "EndAt must be after BeginAt";
    String dateOne();
    String dateTwo();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

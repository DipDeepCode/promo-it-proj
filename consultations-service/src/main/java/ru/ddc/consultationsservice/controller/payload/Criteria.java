package ru.ddc.consultationsservice.controller.payload;

import org.springframework.data.jpa.domain.Specification;

public interface Criteria<T> {
    Specification<T> getSpecification();
}

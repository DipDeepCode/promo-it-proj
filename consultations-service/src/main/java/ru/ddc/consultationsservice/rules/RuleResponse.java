package ru.ddc.consultationsservice.rules;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RuleResponse {
    private boolean allowed;
    private String reason;
}

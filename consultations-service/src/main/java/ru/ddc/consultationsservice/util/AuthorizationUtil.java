package ru.ddc.consultationsservice.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Map;
import java.util.UUID;

public class AuthorizationUtil {

    public static UUID getCurrentUserSubjectId() { // TODO добавить обработку исключений
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        Map<String, Object> tokenAttributes = ((JwtAuthenticationToken) authentication).getTokenAttributes();
        String subjectId = (String) tokenAttributes.get("sub");
        return UUID.fromString(subjectId);
    }
}

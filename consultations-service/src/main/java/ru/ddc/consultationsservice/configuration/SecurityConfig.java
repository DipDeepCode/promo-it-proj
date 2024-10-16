package ru.ddc.consultationsservice.configuration;

import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Stream;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           Converter<Jwt, ? extends AbstractAuthenticationToken> jwtConverter) throws Exception {
        return http
                .csrf(withDefaults())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter)))
                .build();
    }

    @Component
    static class SpringAddonsJwtAuthenticationConverter implements Converter<Jwt, JwtAuthenticationToken> {

        @Override
        public JwtAuthenticationToken convert(Jwt jwt) {
            Collection<? extends GrantedAuthority> authorities = new JwtGrantedAuthoritiesConverter().convert(jwt);
            final String username = JsonPath.read(jwt.getClaims(), "preferred_username");
            return new JwtAuthenticationToken(jwt, authorities, username);
        }
    }

    @RequiredArgsConstructor
    static class JwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<? extends GrantedAuthority>> {

        @Override
        @SuppressWarnings({ "rawtypes", "unchecked" })
        public Collection<? extends GrantedAuthority> convert(Jwt jwt) {
            return Stream.of("$.realm_access.roles", "$.resource_access.*.roles")
                    .flatMap(claimPaths -> {
                        Object claim;
                        try {
                            claim = JsonPath.read(jwt.getClaims(), claimPaths);
                        } catch (PathNotFoundException e) {
                            claim = null;
                        }
                        if (claim == null) {
                            return Stream.empty();
                        }
                        if (claim instanceof String claimStr) {
                            return Stream.of(claimStr.split(","));
                        }
                        if (claim instanceof String[] claimArr) {
                            return Stream.of(claimArr);
                        }
                        if (Collection.class.isAssignableFrom(claim.getClass())) {
                            final var iter = ((Collection) claim).iterator();
                            if (!iter.hasNext()) {
                                return Stream.empty();
                            }
                            final var firstItem = iter.next();
                            if (firstItem instanceof String) {
                                return (Stream<String>) ((Collection) claim).stream();
                            }
                            if (Collection.class.isAssignableFrom(firstItem.getClass())) {
                                return (Stream<String>) ((Collection) claim).stream().flatMap(colItem -> ((Collection) colItem).stream()).map(String.class::cast);
                            }
                        }
                        return Stream.empty();
                    })
                    .map(SimpleGrantedAuthority::new)
                    .map(GrantedAuthority.class::cast)
                    .toList();
        }
    }
}

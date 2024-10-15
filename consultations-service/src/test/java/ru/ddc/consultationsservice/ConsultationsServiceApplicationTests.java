package ru.ddc.consultationsservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@SpringBootTest
class ConsultationsServiceApplicationTests {

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void contextLoads() {
    }
}

package io.nncdevel.example.auth;

import com.azure.spring.cloud.autoconfigure.implementation.aad.configuration.AadAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.security.oauth2.client.servlet.OAuth2ClientAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Basic application tests for the Microsoft Entra ID authentication example.
 *
 * These tests verify that the Spring Boot application context loads correctly
 * and that the basic configuration is valid.
 */
@SpringBootTest(excludeName = {
    "com.azure.spring.cloud.autoconfigure.implementation.aad.configuration.AadAutoConfiguration",
    "com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadOAuth2AutoConfiguration"
})
class ApplicationTests {

    /**
     * Test that the application context loads successfully.
     *
     * This is a basic smoke test that verifies the Spring Boot application
     * can start up without errors and all beans are properly configured.
     */
    @Test
    void contextLoads() {
        // This test will pass if the application context loads successfully
    }
}

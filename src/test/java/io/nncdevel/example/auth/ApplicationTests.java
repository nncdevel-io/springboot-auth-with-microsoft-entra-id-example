package io.nncdevel.example.auth;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

/**
 * Basic application tests for the Microsoft Entra ID authentication example.
 *
 * These tests verify that the Spring Boot application context loads correctly
 * and that the basic configuration is valid.
 */
@SpringBootTest
@TestPropertySource(properties = {
    "spring.cloud.azure.active-directory.enabled=false"
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

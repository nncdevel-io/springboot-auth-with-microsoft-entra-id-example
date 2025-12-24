package io.nncdevel.example.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot application main class for Microsoft Entra ID authentication example.
 *
 * This application demonstrates how to integrate Microsoft Entra ID (formerly Azure AD)
 * authentication with a Spring Boot web application.
 */
@SpringBootApplication
public class Application {

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

package io.nncdevel.example.auth.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Unit tests for ProfileController.
 *
 * These tests verify the behavior of the profile page controller
 * with different authentication states and user attributes.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ProfileControllerTests {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test that the profile page requires authentication.
     * In test environment without OAuth2, this returns 403 Forbidden instead of redirect.
     */
    @Test
    @WithAnonymousUser
    void profilePageRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().isForbidden());
    }

    /**
     * Test that the profile page displays user information for authenticated users with OAuth2.
     */
    @Test
    void profilePageDisplaysUserInformationWithOAuth2() throws Exception {
        mockMvc.perform(get("/profile")
                .with(oauth2Login()
                    .attributes(attrs -> {
                        attrs.put("name", "Test User");
                        attrs.put("email", "test@example.com");
                        attrs.put("oid", "12345-67890");
                    })))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("userName", "Test User"))
                .andExpect(model().attribute("userEmail", "test@example.com"))
                .andExpect(model().attribute("userId", "12345-67890"))
                .andExpect(model().attributeExists("attributes"));
    }

    /**
     * Test that the profile page handles missing email by using preferred_username.
     */
    @Test
    void profilePageUsesPreferredUsernameWhenEmailMissing() throws Exception {
        mockMvc.perform(get("/profile")
                .with(oauth2Login()
                    .attributes(attrs -> {
                        attrs.put("name", "Test User");
                        attrs.put("preferred_username", "testuser@example.com");
                        attrs.put("sub", "subject-id");
                    })))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("userName", "Test User"))
                .andExpect(model().attribute("userEmail", "testuser@example.com"))
                .andExpect(model().attribute("userId", "subject-id"));
    }

    /**
     * Test that the profile page handles missing attributes with default values.
     */
    @Test
    void profilePageHandlesMissingAttributesWithDefaults() throws Exception {
        mockMvc.perform(get("/profile")
                .with(oauth2Login()))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attribute("userName", "Unknown"))
                .andExpect(model().attribute("userEmail", "Not available"))
                .andExpect(model().attribute("userId", "Not available"));
    }
}

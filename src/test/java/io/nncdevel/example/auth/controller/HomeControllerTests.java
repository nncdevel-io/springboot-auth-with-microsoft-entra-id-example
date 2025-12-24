package io.nncdevel.example.auth.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Unit tests for HomeController.
 *
 * These tests verify the behavior of the home page controller
 * with different authentication states.
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.cloud.azure.active-directory.enabled=false"
})
class HomeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test that the home page is accessible to anonymous users.
     */
    @Test
    @WithAnonymousUser
    void homePageAccessibleToAnonymousUsers() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("authenticated", false));
    }

    /**
     * Test that the home page shows authenticated user information.
     */
    @Test
    @WithMockUser
    void homePageShowsAuthenticatedUserInfo() throws Exception {
        mockMvc.perform(get("/")
                .with(oauth2Login()
                    .attributes(attrs -> {
                        attrs.put("name", "Test User");
                        attrs.put("email", "test@example.com");
                    })))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("authenticated", true))
                .andExpect(model().attributeExists("userName"));
    }

    /**
     * Test that the home page handles missing name attribute by using preferred_username.
     */
    @Test
    @WithMockUser
    void homePageUsesPreferredUsernameWhenNameMissing() throws Exception {
        mockMvc.perform(get("/")
                .with(oauth2Login()
                    .attributes(attrs -> {
                        attrs.put("preferred_username", "testuser@example.com");
                    })))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attribute("authenticated", true))
                .andExpect(model().attributeExists("userName"));
    }
}

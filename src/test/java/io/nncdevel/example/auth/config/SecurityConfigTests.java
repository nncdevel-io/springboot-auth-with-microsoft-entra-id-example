package io.nncdevel.example.auth.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Integration tests for SecurityConfig.
 *
 * These tests verify that the security configuration is properly set up
 * and that endpoints have the correct access controls.
 */
@SpringBootTest(excludeName = {
    "com.azure.spring.cloud.autoconfigure.implementation.aad.configuration.AadAutoConfiguration",
    "com.azure.spring.cloud.autoconfigure.implementation.aad.security.AadOAuth2AutoConfiguration"
})
@AutoConfigureMockMvc
class SecurityConfigTests {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Test that the home page is accessible without authentication.
     */
    @Test
    @WithAnonymousUser
    void homePageIsPublic() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    /**
     * Test that CSS files are accessible without authentication.
     */
    @Test
    @WithAnonymousUser
    void cssFilesArePublic() throws Exception {
        mockMvc.perform(get("/css/style.css"))
                .andExpect(status().isOk());
    }

    /**
     * Test that the profile page requires authentication.
     */
    @Test
    @WithAnonymousUser
    void profilePageRequiresAuthentication() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(status().is3xxRedirection());
    }

    /**
     * Test that authenticated users can access the profile page.
     */
    @Test
    @WithMockUser
    void authenticatedUsersCanAccessProfile() throws Exception {
        mockMvc.perform(get("/profile")
                .with(oauth2Login()
                    .attributes(attrs -> {
                        attrs.put("name", "Test User");
                    })))
                .andExpect(status().isOk());
    }

    /**
     * Test that the error page is accessible without authentication.
     */
    @Test
    @WithAnonymousUser
    void errorPageIsPublic() throws Exception {
        mockMvc.perform(get("/error"))
                .andExpect(status().isOk());
    }
}

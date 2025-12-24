package io.nncdevel.example.auth.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

/**
 * Controller for the user profile page.
 *
 * This controller handles authenticated requests to the /profile endpoint
 * and displays user information obtained from Microsoft Entra ID.
 */
@Controller
public class ProfileController {

    /**
     * Handles requests to the profile page.
     *
     * This endpoint is protected and requires authentication.
     * It retrieves user information from the OAuth2 authentication token
     * and adds it to the model for display.
     *
     * @param model the model to add attributes to
     * @param authentication the OAuth2 authentication token
     * @return the name of the view template to render ("profile")
     */
    @GetMapping("/profile")
    public String profile(Model model, OAuth2AuthenticationToken authentication) {
        if (authentication != null) {
            OAuth2User oauth2User = authentication.getPrincipal();
            Map<String, Object> attributes = oauth2User.getAttributes();

            // Extract user information from OAuth2 attributes
            String name = (String) attributes.getOrDefault("name", "Unknown");
            String email = (String) attributes.getOrDefault("email",
                    attributes.getOrDefault("preferred_username", "Not available"));
            String userId = (String) attributes.getOrDefault("oid",
                    attributes.getOrDefault("sub", "Not available"));

            // Add user information to the model
            model.addAttribute("userName", name);
            model.addAttribute("userEmail", email);
            model.addAttribute("userId", userId);
            model.addAttribute("attributes", attributes);
        }

        return "profile";
    }
}

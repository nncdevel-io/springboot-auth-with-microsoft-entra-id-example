package io.nncdevel.example.auth.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for the home page.
 *
 * This controller handles requests to the root path ("/") and displays
 * different content based on the user's authentication status.
 */
@Controller
public class HomeController {

    /**
     * Handles requests to the home page.
     *
     * If the user is authenticated, their name will be added to the model.
     * Otherwise, the page will display a login link.
     *
     * @param model the model to add attributes to
     * @param authentication the current authentication object (may be null if not authenticated)
     * @return the name of the view template to render ("index")
     */
    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()
                && authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) authentication;
            OAuth2User oauth2User = oauth2Token.getPrincipal();

            // Add user information to the model
            String name = oauth2User.getAttribute("name");
            if (name == null) {
                name = oauth2User.getAttribute("preferred_username");
            }

            model.addAttribute("authenticated", true);
            model.addAttribute("userName", name);
        } else {
            model.addAttribute("authenticated", false);
        }

        return "index";
    }
}

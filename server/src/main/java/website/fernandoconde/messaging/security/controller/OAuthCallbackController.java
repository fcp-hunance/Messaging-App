package website.fernandoconde.messaging.security.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class OAuthCallbackController {

    public OAuthCallbackController(OAuth2AuthorizedClientService authorizedClientService) {
    }

    @GetMapping("/callback/github")
    public String handleGitHubCallback() {
        return "Callback from GitHUb";
    }


}


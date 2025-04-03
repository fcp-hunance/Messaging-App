package website.fernandoconde.messaging.security.controller;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class OAuthCallbackController {

    private final OAuth2AuthorizedClientService authorizedClientService;

    public OAuthCallbackController(OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @GetMapping("/callback/github")
    public String handleGitHubCallback(@RequestParam("code") String code) {
        return "redirect:/somewhere";
    }

    @GetMapping("/github")
    public Map<String, Object> oauthCallback(OAuth2AuthenticationToken authentication) {
        OAuth2AuthorizedClient authorizedClient = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName()
        );

        String accessToken = authorizedClient.getAccessToken().getTokenValue();
        return Map.of(
                "username", authentication.getPrincipal().getAttribute("name"),
                "email", authentication.getPrincipal().getAttribute("email"),
                "token", accessToken
        );
    }
}

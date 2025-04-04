package website.fernandoconde.messaging.security.services;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import website.fernandoconde.messaging.model.User;
import website.fernandoconde.messaging.repositories.UserRepository;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 1. Fetch standard OAuth2 user data
        OAuth2User oauth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oauth2User.getAttributes();

        // 2. Check if user exists in DB
        String provider = userRequest.getClientRegistration().getRegistrationId(); // "github"
        String providerId = oauth2User.getName(); // GitHub user ID
        String email = (String) attributes.get("email");
        String username = (String) attributes.get("login"); // GitHub username

        Optional<User> existingUser = userRepository.findByProviderAndProviderId(provider, providerId);

        User user = existingUser.orElseGet(() -> {
            // 3. Create new user if not found
            User newUser = new User(
                    provider,
                    providerId,
                    email != null ? email : username + "@github.com",
                    Collections.singleton(UserRole.ROLE_USER)
            );
            newUser.setUsername(username); // Set GitHub login as username
            return userRepository.save(newUser);
        });

        // 4. Return Spring Security-compatible user
        return new CustomOAuth2User(user, attributes);
    }
}
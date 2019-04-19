package ru.dkt.config;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import ru.dkt.model.Role;
import ru.dkt.model.UserAccount;
import ru.dkt.model.repository.UserAccountRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

final public class CustomTokenEnhancer implements TokenEnhancer {

    final private UserAccountRepository userRepository;

    public CustomTokenEnhancer(UserAccountRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        final User user = (User) authentication.getPrincipal();
        final UserAccount userAccount = userRepository.findByUsernameIgnoreCase(user.getUsername());
        final Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put("roles", userAccount.getRoles().stream().map(Role::getName).collect(Collectors.toList()));
        additionalInfo.put("first_name", userAccount.getFirstName());
        additionalInfo.put("last_name", userAccount.getLastName());
        additionalInfo.put("email", userAccount.getEmail());
        additionalInfo.put("id", userAccount.getId());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}

package ru.dkt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;

@Configuration
public class OAuth2SecurityConfiguration extends AuthorizationServerSecurityConfiguration {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        String tokenEndpointPath = "/oauth/token/revoke";
        http.authorizeRequests()
                .antMatchers(tokenEndpointPath).fullyAuthenticated()
                .and()
                .requestMatchers()
                .antMatchers(tokenEndpointPath)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
    }
}

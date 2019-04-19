package ru.dkt.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OauthUserClientDetails extends ClientDetailsUserDetailsService {

    @Autowired
    public OauthUserClientDetails(ClientDetailsService clientDetailsService) {
        super(clientDetailsService);
    }
}
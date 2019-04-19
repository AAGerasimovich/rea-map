package ru.dkt.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import ru.dkt.model.Status;

import javax.annotation.Resource;

@FrameworkEndpoint
public class RevokeTokenEndpoint {

    @Resource(name = "tokenServices")
    private final ConsumerTokenServices tokenServices;

    @Autowired
    public RevokeTokenEndpoint(ConsumerTokenServices tokenServices) {
        this.tokenServices = tokenServices;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/oauth/token/revoke")
    @ResponseBody
    public Status revoke(@RequestParam("token") String tokenValue) {
        boolean status = tokenServices.revokeToken(tokenValue);
        return new Status(status);
    }
}
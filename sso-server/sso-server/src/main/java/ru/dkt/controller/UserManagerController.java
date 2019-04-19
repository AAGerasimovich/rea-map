package ru.dkt.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.*;
import ru.dkt.model.UserAccount;
import ru.dkt.service.UserCrudService;
import ru.dkt.service.UserManagerService;

@RestController
@SessionAttributes("authorizationRequest")
@EnableResourceServer
@RequestMapping("/manage/users")
@Slf4j
public class UserManagerController extends UserController {

    private final UserManagerService userManagerService;

    public UserManagerController(UserCrudService userCrudService,
                                 UserManagerService userManagerService) {
        super(userCrudService);
        this.userManagerService = userManagerService;
    }

    @PutMapping(value = "/{id}/update")
    public UserAccount updateUser(@PathVariable(value = "id") Integer id,
                                  @RequestBody UserAccount userAccount) {
        userAccount.setId(id);
        log.info("Trying to update user partially: {}", userAccount);
        return userManagerService.updateUserPartially(userAccount);
    }

    @PostMapping(value = "/create")
    public UserAccount createUser(@RequestBody UserAccount userAccount) {
        log.info("Trying to create user: {}", userAccount);
        return userManagerService.createUser(userAccount);
    }
}

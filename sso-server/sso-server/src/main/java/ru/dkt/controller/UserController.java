package ru.dkt.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;
import ru.dkt.model.IdAndEmail;
import ru.dkt.model.UserAccount;
import ru.dkt.service.UserCrudService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@RestController
@SessionAttributes("authorizationRequest")
@EnableResourceServer
@RequiredArgsConstructor
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserCrudService userCrudService;

    @GetMapping("/current")
    public UserAccount getCurrentUser(OAuth2Authentication user) {
        String username = user.getUserAuthentication().getName();
        log.info("Get user for username: {}", username);
        return userCrudService.getUserRepository().findByUsernameIgnoreCase(username);
    }

    @GetMapping("/emails")
    public Collection<IdAndEmail> getEmailsUsers() {
        return userCrudService.getUserRepository().findBy();
    }


    @GetMapping(value = {"", "/"})
    public Iterable<UserAccount> getUsers() {
        log.info("Get all users");
        return userCrudService.getUserRepository().findAllByOrderByUsernameAsc();
    }


    @GetMapping(value = "/{id}")
    public UserAccount getUserById(@PathVariable(name = "id") @NotNull Integer id,
                                   HttpServletResponse response) throws Exception {
        log.info("Get user by id: {}", id);
        Optional<UserAccount> user = userCrudService.getUserRepository().findById(id);
        if (!user.isPresent()) {
            log.info("User not found, id: {}", id);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "User not found, id: " + id);
            return null;
        } else {
            return user.get();
        }
    }

    @PutMapping(value = "/{id}/update")
    public UserAccount updateUser(@PathVariable(value = "id") Integer id, @RequestBody UserAccount userAccount) {
        userAccount.setId(id);
        log.info("Trying to update user: {}", userAccount);
        return userCrudService.updateUser(userAccount);
    }

    @PatchMapping(value = "/{id}/update")
    public UserAccount updateUserPartially(@PathVariable(value = "id") Integer id,
                                           @RequestBody UserAccount userAccount) {
        userAccount.setId(id);
        log.info("Trying to update user partially: {}", userAccount);
        return userCrudService.updateUserPartially(userAccount);
    }

    @PostMapping(value = "/create")
    public UserAccount createUser(@RequestBody UserAccount userAccount) {
        log.info("Trying to create user: {}", userAccount);
        return userCrudService.createUser(userAccount);
    }

    @DeleteMapping(value = "/{id}/delete")
    public Map<String, Boolean> deleteUserById(@PathVariable(name = "id") @NotNull Integer id,
                                               HttpServletResponse response) throws Exception {
        log.info("Delete user by id: {}", id);
        UserAccount user = getUserById(id, response);
        if (user != null) {
            userCrudService.getUserRepository().delete(user);
            return Collections.singletonMap("result", true);
        } else {
            log.info("User not found with id: {}", id);
            return Collections.singletonMap("result", false);
        }
    }
}

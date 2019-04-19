package ru.dkt.service;

import lombok.AllArgsConstructor;
import org.assertj.core.util.Strings;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.dkt.model.UserAccount;

@Service
@AllArgsConstructor
public class UserManagerService {

    private final UserCrudService userCrudService;
    private final PasswordEncoder passwordEncoder;

    public UserAccount updateUserPartially(UserAccount user) {
        encodePassword(user);
        return userCrudService.updateUserPartially(user);
    }

    public UserAccount createUser(UserAccount user) {
        encodePassword(user);
        return userCrudService.createUser(user);
    }

    private void encodePassword(UserAccount user) {
        if (! Strings.isNullOrEmpty(user.getPassword())) {
            String hash = passwordEncoder.encode(user.getPassword());
            user.setPassword(hash);
        }
    }
}

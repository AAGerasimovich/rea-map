package ru.dkt.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.assertj.core.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dkt.model.Role;
import ru.dkt.model.UserAccount;
import ru.dkt.model.repository.RoleRepository;
import ru.dkt.model.repository.UserAccountRepository;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Getter
@RequiredArgsConstructor
public class UserCrudService {
    private final UserAccountRepository userRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public UserAccount updateUser(UserAccount userAccount) {
        return saveUserAccount(userAccount);
    }

    @Transactional
    public UserAccount updateUserPartially(UserAccount userAccount) {
        UserAccount user = userRepository.findById(userAccount.getId()).get();
        user.setEmail(userAccount.getEmail());
        user.setRoles(userAccount.getRoles());
        user.setFirstName(userAccount.getFirstName());
        user.setLastName(userAccount.getLastName());
        user.setUsername(userAccount.getUsername());
        if (!Strings.isNullOrEmpty(userAccount.getPassword())) {
            user.setPassword(userAccount.getPassword());
        }
        return saveUserAccount(user);
    }

    @Transactional
    public UserAccount createUser(UserAccount userAccount) {
        if (userRepository.findByUsernameIgnoreCase(userAccount.getUsername()) != null) {
            throw new UnsupportedOperationException("User with this nickname already exist");
        }

        return saveUserAccount(userAccount);
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private UserAccount saveUserAccount(UserAccount userAccount) {
        if (userAccount.getRoles() != null) {
            List<Role> roles = roleRepository.findByNameIn(userAccount.getRoles()
                    .stream()
                    .map(Role::getName)
                    .collect(Collectors.toList())
            );

            userAccount.setRoles(roles);
        }

        return userRepository.save(userAccount);
    }
}

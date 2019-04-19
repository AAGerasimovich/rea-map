package ru.dkt.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.dkt.model.Privilege;
import ru.dkt.model.Role;
import ru.dkt.model.repository.PrivilegeRepository;
import ru.dkt.model.repository.RoleRepository;
import ru.dkt.model.repository.UserAccountRepository;

import java.util.Collection;


@Component
@Profile("dev")
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private boolean alreadySetup = false;

    @Autowired
    private UserAccountRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }
//
//        // == create initial privileges
//        Privilege readPrivilege = createPrivilegeIfNotFound("READ_PRIVILEGE");
//        Privilege writePrivilege = createPrivilegeIfNotFound("WRITE_PRIVILEGE");
//
//        // == create initial roles
//        List<Privilege> adminPrivileges = Arrays.asList(readPrivilege, writePrivilege);
//        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
//        List<Privilege> rolePrivileges = new ArrayList<>();
//        createRoleIfNotFound("ROLE_USER", rolePrivileges);
//
//        Role adminRole = roleRepository.findByName("ROLE_ADMIN");
//        UserAccount user = new UserAccount();
//        user.setFirstName("Admin");
//        user.setLastName("Admin");
//        user.setUsername("Admin");
//        user.setEmail("admin@test.com");
//        user.setPassword(passwordEncoder.encodePassword("admin", null));
//        user.setRoles(Arrays.asList(adminRole));
//        user.setEnabled(true);
//        userRepository.save(user);
//
//        Role basicRole = roleRepository.findByName("ROLE_USER");
//        UserAccount basicUser = new UserAccount();
//        basicUser.setFirstName("User");
//        basicUser.setLastName("User");
//        basicUser.setUsername("user");
//        basicUser.setEmail("user@test.com");
//        basicUser.setPassword(passwordEncoder.encodePassword("Bka3UnYusZ", null));
//        basicUser.setRoles(Arrays.asList(basicRole));
//        basicUser.setEnabled(true);
//        userRepository.save(basicUser);

        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }

}
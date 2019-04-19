package ru.dkt.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dkt.model.Role;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(String name);
    List<Role> findByNameIn(List<String> names);

    void delete(Role role);

}

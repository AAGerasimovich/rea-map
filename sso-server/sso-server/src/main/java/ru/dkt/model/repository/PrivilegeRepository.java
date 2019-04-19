package ru.dkt.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dkt.model.Privilege;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Integer> {

    Privilege findByName(String name);

    void delete(Privilege privilege);

}

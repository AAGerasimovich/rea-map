package ru.dkt.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.dkt.model.IdAndEmail;
import ru.dkt.model.UserAccount;

import java.util.List;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {

    List<UserAccount> findAllByOrderByIdAsc();

    List<UserAccount> findAllByOrderByUsernameAsc();

    UserAccount findByUsernameIgnoreCase(String name);

    List<IdAndEmail> findBy();

    void delete(UserAccount user);

}




package nl.inholland.gnasBank.repositories;

import nl.inholland.gnasBank.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByFirstNameAndLastName(String firstName, String lastName);
    Optional<User> findUserByEmail(String email);
    Page<User> findAll(Pageable pageable);
    Page<User> findAllByBankAccountsIsNull(Pageable pageable);
}

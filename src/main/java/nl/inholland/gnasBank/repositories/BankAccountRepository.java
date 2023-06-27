package nl.inholland.gnasBank.repositories;

import nl.inholland.gnasBank.models.AccountType;
import nl.inholland.gnasBank.models.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
//    @Query("SELECT ba FROM BankAccount ba JOIN ba.user u WHERE LOWER(u.firstName) = LOWER(:firstName) AND LOWER(u.lastName) = LOWER(:lastName)")
//    Iterable<BankAccount> findBankAccountByUserFirstNameIgnoreCaseAndUserLastNameIgnoreCase(@Param("firstName") String firstName, @Param("lastName") String lastName);
    //im kinda proud of this, but it can be done much easier by just calling for the UserService and getting bank accounts of that user


    Page<BankAccount> findAll(Pageable pageable);
}

package nl.inholland.gnasBank.repositories;

import nl.inholland.gnasBank.models.BankAccount;
import nl.inholland.gnasBank.models.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Page<Transaction> findByAccountFrom(BankAccount account, Pageable pageable);
    Page<Transaction> findByAccountTo(BankAccount account, Pageable pageable);
    Page<Transaction> findAll(Pageable pageable);

    @Query("SELECT SUM(t.amount) FROM Transaction t WHERE t.accountFrom.iban = :accountIBAN AND t.timeStamp  = :date")
    Double getSumOfAmountsByAccountAndDate(String accountIBAN, LocalDateTime date);

    Iterable<Transaction> findByAccountFromAndAmountLessThan(BankAccount account, double amount);
    Iterable<Transaction> findByAccountFromAndAmountEquals(BankAccount account, double amount);
    Iterable<Transaction> findByAccountFromAndAmountGreaterThan(BankAccount account, double amount);
    Iterable<Transaction> findByAccountFromAndTimeStampBetween(BankAccount account, LocalDateTime startDate, LocalDateTime endDate);

    Iterable<Transaction> findByAccountToAndAmountLessThan(BankAccount account, double amount);
    Iterable<Transaction> findByAccountToAndAmountEquals(BankAccount account, double amount);
    Iterable<Transaction> findByAccountToAndAmountGreaterThan(BankAccount account, double amount);
    Iterable<Transaction> findByAccountToAndTimeStampBetween(BankAccount account, LocalDateTime startDate, LocalDateTime endDate);
}

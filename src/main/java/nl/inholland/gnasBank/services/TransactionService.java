package nl.inholland.gnasBank.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import nl.inholland.gnasBank.models.AccountType;
import nl.inholland.gnasBank.models.BankAccount;
import nl.inholland.gnasBank.models.Transaction;
import nl.inholland.gnasBank.models.User;
import nl.inholland.gnasBank.models.dto.TransactionDTO;
import nl.inholland.gnasBank.repositories.TransactionRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private TransactionRepository repository;
    private BankAccountService bankAccountService;

    public TransactionService(TransactionRepository repository, BankAccountService bankAccountService) {
        this.repository = repository;
        this.bankAccountService = bankAccountService;
    }

    public List<Transaction> getAll(Integer page, Integer size) {
        PageRequest pageable = PageRequest.of(page, size);
        return repository.findAll(pageable).getContent().stream().toList();
    }

    public Transaction getById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Transaction with id " + id + " not found"));
    }
    public List<Transaction> getByAccountFrom(BankAccount account, Integer page, Integer size) {
        PageRequest pageable = PageRequest.of(page, size);
        return repository.findByAccountFrom(account, pageable).getContent();
    }

    public List<Transaction> getByAccountTo(BankAccount account, Integer page, Integer size) {
        PageRequest pageable = PageRequest.of(page, size);
        return repository.findByAccountTo(account, pageable).getContent();
    }

    public Double getSumOfAmountsByAccountAndDate(String accountIBAN, LocalDateTime date) {
        return repository.getSumOfAmountsByAccountAndDate(accountIBAN, date);
    }

    public List<Transaction> getByAccountFromAndAmountLessThan(BankAccount account, double amount) {
        return (List<Transaction>) repository.findByAccountFromAndAmountLessThan(account, amount);
    }

    public List<Transaction> getByAccountFromAndAmountEquals(BankAccount account, double amount) {
        return (List<Transaction>) repository.findByAccountFromAndAmountEquals(account, amount);
    }

    public List<Transaction> getByAccountFromAndAmountGreaterThan(BankAccount account, double amount) {
        return (List<Transaction>) repository.findByAccountFromAndAmountGreaterThan(account, amount);
    }

    public List<Transaction> getByAccountFromAndTimeStampBetween(BankAccount account, LocalDateTime startDate, LocalDateTime endDate) {
        return (List<Transaction>) repository.findByAccountFromAndTimeStampBetween(account, startDate, endDate);
    }

    public List<Transaction> getByAccountToAndAmountLessThan(BankAccount account, double amount) {
        return (List<Transaction>) repository.findByAccountToAndAmountLessThan(account, amount);
    }

    public List<Transaction> getByAccountToAndAmountEquals(BankAccount account, double amount) {
        return (List<Transaction>) repository.findByAccountToAndAmountEquals(account, amount);
    }

    public List<Transaction> getByAccountToAndAmountGreaterThan(BankAccount account, double amount) {
        return (List<Transaction>) repository.findByAccountToAndAmountGreaterThan(account, amount);
    }

    public List<Transaction> getByAccountToAndTimeStampBetween(BankAccount account, LocalDateTime startDate, LocalDateTime endDate) {
        return (List<Transaction>) repository.findByAccountToAndTimeStampBetween(account, startDate, endDate);
    }

    public Transaction create(TransactionDTO transactionDTO) {
        BankAccount accountFrom = bankAccountService.getById(transactionDTO.getIbanAccountFrom());
        BankAccount accountTo = bankAccountService.getById(transactionDTO.getIbanAccountTo());

        // Check if accounts belong to the same user (rule: CURRENT to SAVINGS or vice versa)
        if (!accountFrom.getUser().equals(accountTo.getUser()) &&
                (accountFrom.getType() != AccountType.CURRENT || accountTo.getType() != AccountType.CURRENT)) {
            throw new IllegalArgumentException("Inter-user transactions can only occur between CURRENT accounts");
        }

        // Check if the transaction exceeds the day limit for the user
        if (exceedsDayLimit(accountFrom, transactionDTO.getAmount())) {
            throw new IllegalArgumentException("Transaction exceeds the day limit for the user");
        }

        // Check if the bank account balance goes below the absolute limit
        if (accountFrom.getBalance() - transactionDTO.getAmount() < accountFrom.getAbsoluteLimit()) {
            throw new IllegalArgumentException("Transaction cannot be performed as it exceeds the account's absolute limit");
        }

        // Check if the user exceeds the transaction limit
        if (transactionDTO.getAmount() > accountFrom.getUser().getTransactionLimit()) {
            throw new IllegalArgumentException("Transaction exceeds the user's transaction limit");
        }

        // Create the transaction
        Transaction transaction = new Transaction();
        transaction.setAccountFrom(accountFrom);
        transaction.setAccountTo(accountTo);
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTimeStamp(LocalDateTime.now());

        accountFrom.setBalance(accountFrom.getBalance() - transactionDTO.getAmount());
        accountTo.setBalance(accountTo.getBalance() + transactionDTO.getAmount());

        bankAccountService.updateFromBankAccount(accountFrom);
        bankAccountService.updateFromBankAccount(accountTo);

        try {
            return repository.save(transaction);
        } catch (DataAccessException | PersistenceException err) {
            throw new RuntimeException("Failed to create transaction: " + err.getMessage());
        }
    }

    public boolean exceedsDayLimit(BankAccount account, double transactionAmount) {
        LocalDateTime currentDate = LocalDateTime.now();
        Double sumOfAmounts = this.getSumOfAmountsByAccountAndDate(account.getIban(), currentDate);
        return sumOfAmounts != null && sumOfAmounts + transactionAmount > account.getUser().getDayLimit();
    }

    public Transaction update(Transaction transaction) {
        try {
            return repository.save(transaction);
        } catch (DataAccessException | PersistenceException e) {
            throw new RuntimeException("Failed to update transaction: " + e.getMessage());
        }
    }

    public boolean delete(UUID id) {
        try {
            repository.deleteById(id);
            return true;
        } catch (DataAccessException | PersistenceException e) {
            throw new RuntimeException("Failed to delete transaction: " + e.getMessage());
        }
    }
}

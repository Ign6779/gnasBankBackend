package nl.inholland.gnasBank.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import nl.inholland.gnasBank.models.AccountType;
import nl.inholland.gnasBank.models.BankAccount;
import nl.inholland.gnasBank.models.User;
import nl.inholland.gnasBank.models.dto.BankAccountDTO;
import nl.inholland.gnasBank.repositories.BankAccountRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BankAccountService {
    private BankAccountRepository repository;
    private UserService userService;

    public BankAccountService(BankAccountRepository repository, UserService userService) {
        this.repository = repository;
        this.userService = userService;
    }

    public List<BankAccount> getAll(Integer page, Integer size) {
        PageRequest pageable = PageRequest.of(page, size);
        return repository.findAll(pageable)
                .getContent()
                .stream()
                .filter(bankAccount -> bankAccount.getType().equals(AccountType.CURRENT) || bankAccount.getType().equals(AccountType.SAVINGS))
                .collect(Collectors.toList());
    }

    public BankAccount getById(String iban) {
        return repository.findById(iban)
                .orElseThrow(() -> new EntityNotFoundException("Bank account with IBAN " + iban + " not found"));
    }

    public List<BankAccount> getByUsername(String firstName, String lastName) {
        try {
            User user = userService.getByFirstAndLastName(firstName, lastName);
            return user.getBankAccounts();
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Failed to get bank accounts for user: " + e.getMessage());
        }
    }

    public BankAccount create(BankAccountDTO bankAccountDTO) {
        try {
            BankAccount bankAccount = mapDTO(bankAccountDTO);

            String iban;
            do {
                iban = generateIban();
                bankAccount.setIban(iban);
            } while(!repository.findById(iban).isEmpty());

            return repository.save(bankAccount);

        } catch (DataAccessException | PersistenceException err) {
            throw new RuntimeException("Failed to create bank account: " + err.getMessage());
        }
    }

    public BankAccount mapDTO(BankAccountDTO dto) {
        User user = userService.getById(dto.getUserId());

        return new BankAccount(user, dto.getAbsoluteLimit(), dto.getBalance(), dto.getType());
    }

    private String generateIban() {
        String countryCode = "NL";
        String bankCode = "INHO0";
        String accountNumber = generateRandomAccountNumber();
        return countryCode + bankCode + accountNumber;
    }

    private static String generateRandomAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();

        // Generate 9 random digits
        for (int i = 0; i < 9; i++) {
            int digit = random.nextInt(10);
            accountNumber.append(digit);
        }

        return accountNumber.toString();
    }

    public BankAccount update(BankAccountDTO bankAccountDTO) {
        try {
            BankAccount bankAccount = mapDTO(bankAccountDTO);
            return repository.save(bankAccount);
        } catch (DataAccessException | PersistenceException err) {
            throw new RuntimeException("Failed to update bank account: " + err.getMessage());
        }
    }

    public BankAccount updateFromBankAccount(BankAccount bankAccount) {
        try {
            return repository.save(bankAccount);
        } catch (DataAccessException | PersistenceException err) {
            throw new RuntimeException("Failed to update bank account: " + err.getMessage());
        }
    }

    public boolean delete(String iban) {
        try {
            repository.deleteById(iban);
            return true;
        } catch (DataAccessException | PersistenceException err) {
            throw new RuntimeException("Failed to delete bank account: " + err.getMessage());
        }
    }
}

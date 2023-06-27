package nl.inholland.gnasBank.configuration;

import jakarta.transaction.Transactional;
import nl.inholland.gnasBank.models.*;
import nl.inholland.gnasBank.models.dto.BankAccountDTO;
import nl.inholland.gnasBank.repositories.BankAccountRepository;
import nl.inholland.gnasBank.repositories.TransactionRepository;
import nl.inholland.gnasBank.services.BankAccountService;
import nl.inholland.gnasBank.services.TransactionService;
import nl.inholland.gnasBank.services.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Component
public class MyApplicationRunner implements ApplicationRunner {
    private final UserService userService;
    private final BankAccountService bankAccountService;
    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;

    public MyApplicationRunner(UserService userService, BankAccountService bankAccountService, TransactionRepository transactionRepository, BankAccountRepository bankAccountRepository) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        List.of(
                new User("user1@email.com", "test", "The", "Dude", "+31000000000", 99.9, 99.9, List.of(Role.EMPLOYEE, Role.CUSTOMER)),
                new User("user2@email.com", "test", "Jeffrey", "Lebowski", "+31000000001", 99.9, 99.9, List.of(Role.CUSTOMER)),

                new User("user3@email.com", "test", "Maude", "Lebowski", "+31000000002", 99.9, 99.9, List.of(Role.CUSTOMER)),
                new User("user4@email.com", "test", "Walter", "Sobchak", "+31000000002", 99.9, 99.9, List.of(Role.CUSTOMER)),
                new User("user99@email.com", "test", "Theodore", "Donald", "+31000000009", 99.9, 99.9, List.of(Role.CUSTOMER)),
                new User("admin@email.com", "test", "Gnas", "Inholland", "+31000000000", 99.9, 99.9, List.of(Role.EMPLOYEE, Role.CUSTOMER))

        ).forEach(user -> userService.create(user));

        userService.getAll(0, 100, true).forEach(System.out::println);


        List.of(
                new BankAccountDTO(userService.getByFirstAndLastName("Gnas", "Inholland").getUuid(), 100, 1000000000, AccountType.BANK),
                new BankAccountDTO(userService.getByFirstAndLastName("The", "Dude").getUuid(), 0, 100, AccountType.CURRENT),
                new BankAccountDTO(userService.getByFirstAndLastName("The", "Dude").getUuid(), 0, 100.0, AccountType.SAVINGS),
                new BankAccountDTO(userService.getByFirstAndLastName("Jeffrey", "Lebowski").getUuid(), 0, 100.0, AccountType.CURRENT),
                new BankAccountDTO(userService.getByFirstAndLastName("Jeffrey", "Lebowski").getUuid(), 0, 100.0, AccountType.SAVINGS),
                new BankAccountDTO(userService.getByFirstAndLastName("Maude", "Lebowski").getUuid(), 0, 100.0, AccountType.CURRENT),
                new BankAccountDTO(userService.getByFirstAndLastName("Maude", "Lebowski").getUuid(), 0, 100.0, AccountType.SAVINGS),
                new BankAccountDTO(userService.getByFirstAndLastName("Theodore", "Donald").getUuid(), 0, 100.0, AccountType.SAVINGS)

        ).forEach(bankAccount -> bankAccountService.create(bankAccount));

        bankAccountRepository.findAll().forEach(System.out::println);


        List<BankAccount> bankAccounts = (List<BankAccount>) bankAccountRepository.findAll();
        transactionRepository.saveAll(List.of(
                new Transaction(LocalDateTime.of(2023, Month.MARCH, 1, 12, 30, 45), bankAccounts.get(0), bankAccounts.get(1), 100.0, "1cfc38d6-e378-11ed-b5ea-0242ac120002"),
                new Transaction(LocalDateTime.of(2023, Month.MARCH, 2, 12, 30, 45), bankAccounts.get(1), bankAccounts.get(0), 200.0, "1cfc38d6-e378-11ed-b5ea-0242ac120002"),
                new Transaction(LocalDateTime.of(2023, Month.MARCH, 3, 12, 30, 45), bankAccounts.get(2), bankAccounts.get(1), 300.0, "1cfc38d6-e378-11ed-b5ea-0242ac120002"),
                new Transaction(LocalDateTime.of(2023, Month.MARCH, 4, 12, 30, 45), bankAccounts.get(3), bankAccounts.get(1), 400.0, "1cfc38d6-e378-11ed-b5ea-0242ac120002"),
                new Transaction(LocalDateTime.of(2023, Month.MARCH, 5, 12, 30, 45), bankAccounts.get(4), bankAccounts.get(1), 500.0, "1cfc38d6-e378-11ed-b5ea-0242ac120002"),
                new Transaction(LocalDateTime.of(2023, Month.MARCH, 6, 12, 30, 45), bankAccounts.get(5), bankAccounts.get(1), 600.0, "1cfc38d6-e378-11ed-b5ea-0242ac120002"),

                new Transaction(LocalDateTime.of(2023, Month.MARCH, 7, 12, 30, 45), bankAccounts.get(5), bankAccounts.get(1), 700.0, "1cfc38d6-e378-11ed-b5ea-0242ac120002"),
                new Transaction(LocalDateTime.of(2023, Month.MARCH, 8, 12, 30, 45), bankAccounts.get(4), bankAccounts.get(1), 800.0, "1cfc38d6-e378-11ed-b5ea-0242ac120002"),
                new Transaction(LocalDateTime.of(2023, Month.MARCH, 9, 12, 30, 45), bankAccounts.get(3), bankAccounts.get(2), 900.0, "1cfc38d6-e378-11ed-b5ea-0242ac120002")
        ));
        transactionRepository.findAll().forEach(System.out::println);

    }
}

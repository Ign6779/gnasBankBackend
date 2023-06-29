package nl.inholland.gnasBank.services;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.gnasBank.controllers.UserController;
import nl.inholland.gnasBank.models.AccountType;
import nl.inholland.gnasBank.models.BankAccount;
import nl.inholland.gnasBank.models.Transaction;
import nl.inholland.gnasBank.models.User;
import nl.inholland.gnasBank.models.dto.TransactionDTO;
import nl.inholland.gnasBank.repositories.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class TransactionServiceTest {
    private MockMvc mockMvc;

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private BankAccountService bankAccountService;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new TransactionService(transactionRepository, bankAccountService)).build();
    }

    @Test
    void testGetById() throws Exception {
        UUID id = UUID.randomUUID();

        Transaction transaction = new Transaction();
        transaction.setUuid(id);

        when(transactionRepository.findById(id)).thenReturn(Optional.of(transaction));

        Transaction result = transactionService.getById(id);

        Assertions.assertEquals(transaction, result);
    }

    @Test
    void testGetByIdWithInvalidId() throws Exception{
        UUID id = UUID.randomUUID();

        when(transactionRepository.findById(id)).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            transactionService.getById(id);
        });
    }

    @Test
    void testGetByAccountFrom() throws Exception{
        BankAccount account = new BankAccount();

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.add(new Transaction());

        when(transactionRepository.findByAccountFrom(account, PageRequest.of(0, 10))).thenReturn(new PageImpl<>(transactions));

        List<Transaction> result = transactionService.getByAccountFrom(account, 0, 10);

        Assertions.assertEquals(transactions, result);
    }

    @Test
    void testGetByAccountTo() throws Exception{
        BankAccount account = new BankAccount();

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.add(new Transaction());

        when(transactionRepository.findByAccountTo(account, PageRequest.of(0, 10))).thenReturn(new PageImpl<>(transactions));

        List<Transaction> result = transactionService.getByAccountTo(account, 0, 10);

        Assertions.assertEquals(transactions, result);
    }

    @Test
    void testGetSumOfAmountsByAccountAndDate() throws Exception{
        String accountIBAN = "123456789";
        LocalDateTime date = LocalDateTime.now();
        Double expectedSum = 500.0;

        when(transactionRepository.getSumOfAmountsByAccountAndDate(accountIBAN, date)).thenReturn(expectedSum);

        Double result = transactionService.getSumOfAmountsByAccountAndDate(accountIBAN, date);

        Assertions.assertEquals(expectedSum, result);
    }

    @Test
    void testGetByAccountFromAndAmountLessThan() throws Exception{
        BankAccount account = new BankAccount();

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.add(new Transaction());

        double amountThreshold = 500.0;

        when(transactionRepository.findByAccountFromAndAmountLessThan(account, amountThreshold)).thenReturn(transactions);

        List<Transaction> result = transactionService.getByAccountFromAndAmountLessThan(account, amountThreshold);

        Assertions.assertEquals(transactions, result);
    }

    @Test
    void testGetByAccountFromAndAmountEquals() throws Exception{
        BankAccount account = new BankAccount();

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.add(new Transaction());

        double amount = 500.0;

        when(transactionRepository.findByAccountFromAndAmountEquals(account, amount)).thenReturn(transactions);

        List<Transaction> result = transactionService.getByAccountFromAndAmountEquals(account, amount);

        Assertions.assertEquals(transactions, result);
    }

    @Test
    void testGetByAccountFromAndAmountGreaterThan() throws Exception{
        BankAccount account = new BankAccount();

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.add(new Transaction());

        double amountThreshold = 500.0;

        when(transactionRepository.findByAccountFromAndAmountGreaterThan(account, amountThreshold)).thenReturn(transactions);

        List<Transaction> result = transactionService.getByAccountFromAndAmountGreaterThan(account, amountThreshold);

        Assertions.assertEquals(transactions, result);
    }

    @Test
    void testGetByAccountFromAndTimeStampBetween() throws Exception{
        BankAccount account = new BankAccount();

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.add(new Transaction());

        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 1, 31, 23, 59, 59);

        when(transactionRepository.findByAccountFromAndTimeStampBetween(account, startDate, endDate)).thenReturn(transactions);

        List<Transaction> result = transactionService.getByAccountFromAndTimeStampBetween(account, startDate, endDate);

        Assertions.assertEquals(transactions, result);
    }

    @Test
    void testGetByAccountToAndAmountLessThan() throws Exception{
        BankAccount account = new BankAccount();

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.add(new Transaction());

        double amountThreshold = 500.0;

        when(transactionRepository.findByAccountToAndAmountLessThan(account, amountThreshold)).thenReturn(transactions);

        List<Transaction> result = transactionService.getByAccountToAndAmountLessThan(account, amountThreshold);

        Assertions.assertEquals(transactions, result);
    }

    @Test
    void testGetByAccountToAndAmountEquals() throws Exception{

        BankAccount account = new BankAccount();

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.add(new Transaction());

        double amount = 500.0;

        when(transactionRepository.findByAccountToAndAmountEquals(account, amount)).thenReturn(transactions);

        List<Transaction> result = transactionService.getByAccountToAndAmountEquals(account, amount);

        Assertions.assertEquals(transactions, result);
    }

    @Test
    void testGetByAccountToAndAmountGreaterThan() throws Exception{
        BankAccount account = new BankAccount();

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.add(new Transaction());

        double amountThreshold = 500.0;

        when(transactionRepository.findByAccountToAndAmountGreaterThan(account, amountThreshold)).thenReturn(transactions);

        List<Transaction> result = transactionService.getByAccountToAndAmountGreaterThan(account, amountThreshold);

        Assertions.assertEquals(transactions, result);
    }

    @Test
    void testGetByAccountToAndTimeStampBetween() throws Exception{
        BankAccount account = new BankAccount();

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction());
        transactions.add(new Transaction());

        LocalDateTime startDate = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 1, 31, 23, 59, 59);

        when(transactionRepository.findByAccountToAndTimeStampBetween(account, startDate, endDate)).thenReturn(transactions);

        List<Transaction> result = transactionService.getByAccountToAndTimeStampBetween(account, startDate, endDate);

        Assertions.assertEquals(transactions, result);
    }

    @Test
    void testCreate() throws Exception{
        // create sample transaction DTO
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setIbanAccountFrom("accountFromIBAN");
        transactionDTO.setIbanAccountTo("accountToIBAN");
        transactionDTO.setAmount(100.0);

        // create sample bank accounts
        BankAccount accountFrom = new BankAccount();
        accountFrom.setBalance(500.0);
        accountFrom.setAbsoluteLimit(200.0);
        accountFrom.setType(AccountType.CURRENT);

        User user = new User();
        user.setTransactionLimit(150.0);
        user.setDayLimit(500.00);
        accountFrom.setUser(user);

        BankAccount accountTo = new BankAccount();
        accountTo.setBalance(200.0);
        accountTo.setType(AccountType.CURRENT);

        when(bankAccountService.getById("accountFromIBAN")).thenReturn(accountFrom);
        when(bankAccountService.getById("accountToIBAN")).thenReturn(accountTo);

        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Transaction result = transactionService.create(transactionDTO);

        // verify all the transaction properties
        Assertions.assertNotNull(result);
        Assertions.assertEquals(accountFrom, result.getAccountFrom());
        Assertions.assertEquals(accountTo, result.getAccountTo());
        Assertions.assertEquals(100.0, result.getAmount());
        Assertions.assertNotNull(result.getTimeStamp());
        verify(bankAccountService, times(1)).updateFromBankAccount(accountFrom);
        verify(bankAccountService, times(1)).updateFromBankAccount(accountTo);
    }

    @Test
    void testExceedsDayLimit() throws Exception{
        User user = new User();
        user.setTransactionLimit(100.0);
        user.setDayLimit(100.00);

        BankAccount account = new BankAccount();
        account.setIban("accountIBAN");

        account.setUser(user);

        when(transactionRepository.getSumOfAmountsByAccountAndDate("accountIBAN", LocalDateTime.now()))
                .thenReturn(100.0);

        boolean result = transactionService.exceedsDayLimit(account, 150.0);

        Assertions.assertTrue(result);
    }

    @Test
    void testUpdate() throws Exception{
        UUID uuid = UUID.randomUUID();
        Transaction transaction = new Transaction();
        transaction.setUuid(uuid);

        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction result = transactionService.update(transaction);

        Assertions.assertEquals(transaction, result);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void testDelete() throws Exception{
        UUID transactionId = UUID.randomUUID();

        doNothing().when(transactionRepository).deleteById(transactionId);

        boolean result = transactionService.delete(transactionId);

        Assertions.assertTrue(result);
        verify(transactionRepository, times(1)).deleteById(transactionId);
    }
}


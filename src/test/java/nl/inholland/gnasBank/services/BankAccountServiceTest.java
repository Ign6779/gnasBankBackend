package nl.inholland.gnasBank.services;

import nl.inholland.gnasBank.models.AccountType;
import nl.inholland.gnasBank.models.BankAccount;
import nl.inholland.gnasBank.models.User;
import nl.inholland.gnasBank.models.dto.BankAccountDTO;
import nl.inholland.gnasBank.repositories.BankAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BankAccountServiceTest {
    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private BankAccountService bankAccountService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        List<BankAccount> bankAccounts = new ArrayList<>();
        BankAccount ba1 = new BankAccount();
        ba1.setType(AccountType.CURRENT);
        bankAccounts.add(ba1);

        BankAccount ba2 = new BankAccount();
        ba2.setType(AccountType.SAVINGS);
        bankAccounts.add(ba2);

        when(bankAccountRepository.findAll(PageRequest.of(0, 10))).thenReturn(new PageImpl<>(bankAccounts));

        List<BankAccount> result = bankAccountService.getAll(0, 10);

        assertEquals(bankAccounts.size(), result.size());
    }

    @Test
    void testGetById() {
        String iban = "NLINHO0123456789";

        BankAccount bankAccount = new BankAccount();
        bankAccount.setIban(iban);

        when(bankAccountRepository.findById(iban)).thenReturn(Optional.of(bankAccount));

        BankAccount result = bankAccountService.getById(iban);

        assertEquals(bankAccount, result);
    }

    @Test
    void testGetByUsername() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");

        when(userService.getByFirstAndLastName("John", "Doe")).thenReturn(user);

        List<BankAccount> result = bankAccountService.getByUsername("John", "Doe");

        assertEquals(user.getBankAccounts(), result);
    }

    @Test
    void testCreate() {
        UUID userId = UUID.randomUUID();
        BankAccountDTO bankAccountDTO = new BankAccountDTO(userId, 500, 1000, AccountType.CURRENT);
        User user = new User();
        user.setUuid(userId);

        when(userService.getById(userId)).thenReturn(user);

        BankAccount bankAccount = bankAccountService.mapDTO(bankAccountDTO);
        bankAccount.setIban("NLINHO0123456789");

        when(bankAccountRepository.findById(anyString()))
                .thenReturn(Optional.of(bankAccount), Optional.empty());
        when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);

        BankAccount result = bankAccountService.create(bankAccountDTO);

        assertEquals(bankAccount, result);
    }

    @Test
    void testUpdate() {
        UUID userId = UUID.randomUUID();
        BankAccountDTO bankAccountDTO = new BankAccountDTO(userId, 500, 1000, AccountType.CURRENT);
        User user = new User();
        user.setUuid(userId);

        when(userService.getById(userId)).thenReturn(user);

        BankAccount bankAccount = bankAccountService.mapDTO(bankAccountDTO);
        bankAccount.setIban("NLINHO0123456789");

        when(bankAccountRepository.findById(anyString()))
                .thenReturn(Optional.of(bankAccount), Optional.empty());
        when(bankAccountRepository.save(bankAccount)).thenReturn(bankAccount);

        BankAccount result = bankAccountService.update(bankAccountDTO);

        assertEquals(bankAccount, result);
    }



    @Test
    void testDelete() {
        String iban = "NLINHO0123456789";

        doNothing().when(bankAccountRepository).deleteById(iban);

        boolean result = bankAccountService.delete(iban);

        assertTrue(result);
    }
}

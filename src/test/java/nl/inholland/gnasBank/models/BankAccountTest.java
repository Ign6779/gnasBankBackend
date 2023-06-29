package nl.inholland.gnasBank.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BankAccountTest {
    private BankAccount bankAccount;
    private User user;

    @BeforeEach
    void init(){
        user = new User();
        bankAccount = new BankAccount(user, 1000.0, 500.0, AccountType.CURRENT);
    }

    @Test
    void newBankAccountShouldNotBeNull() throws Exception {
        Assertions.assertNotNull(bankAccount);
    }

    @Test
    void bankAccountFieldsShouldBeCorrect() throws Exception {
        Assertions.assertEquals(user, bankAccount.getUser());
        Assertions.assertEquals(1000.0, bankAccount.getAbsoluteLimit());
        Assertions.assertEquals(500.0, bankAccount.getBalance());
        Assertions.assertEquals(AccountType.CURRENT, bankAccount.getType());
    }

    @Test
    void bankAccountShouldBeAvailableByDefault() throws Exception {
        Assertions.assertTrue(bankAccount.isAvailable());
    }
}

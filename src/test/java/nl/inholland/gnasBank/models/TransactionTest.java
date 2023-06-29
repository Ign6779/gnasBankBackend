package nl.inholland.gnasBank.models;

import io.cucumber.java.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionTest {
    private Transaction transaction;

    @BeforeEach
    void init(){
        transaction = new Transaction();
    }

    @Test
    void newTransactionShouldNotBeNull() throws Exception{
        Assertions.assertNotNull(transaction);
    }

    @Test
    void transactionDateShouldBeNow() throws Exception{
        LocalDateTime now = LocalDateTime.now();
        transaction.setTimeStamp(now);

        LocalDate transactionDate = transaction.getTimeStamp().toLocalDate();
        LocalDate currentDate = LocalDate.now();

        Assertions.assertEquals(currentDate, transactionDate);
    }

}

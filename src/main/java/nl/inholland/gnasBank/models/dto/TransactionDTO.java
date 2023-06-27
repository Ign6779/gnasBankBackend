package nl.inholland.gnasBank.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.inholland.gnasBank.models.BankAccount;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransactionDTO {
    private String ibanAccountFrom;
    private String ibanAccountTo;
    private double amount;
//    private LocalDateTime timeStamp;
}

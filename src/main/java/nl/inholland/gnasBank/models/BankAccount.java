package nl.inholland.gnasBank.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class BankAccount {
    @Id
    private String iban;

    @ManyToOne
    @JsonBackReference
    private User user;
    private double absoluteLimit;
    private double balance;
    private AccountType type;
    private boolean available;

    public BankAccount(User user, double absoluteLimit, double balance, AccountType type) {
        this.user = user;
        this.absoluteLimit = absoluteLimit;
        this.balance = balance;
        this.type = type;
        this.available = true;
    }
}

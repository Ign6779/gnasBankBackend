package nl.inholland.gnasBank.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private UUID uuid;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;
    private Double dayLimit;
    private Double transactionLimit;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BankAccount> bankAccounts;

    public User(String email, String password, String firstName, String lastName, String phone, double dayLimit, double transactionLimit, List<Role> roles) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.dayLimit = dayLimit;
        this.transactionLimit = transactionLimit;
        this.roles = roles;
    }
}

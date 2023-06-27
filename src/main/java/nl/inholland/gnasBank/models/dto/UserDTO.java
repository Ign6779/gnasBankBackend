package nl.inholland.gnasBank.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.inholland.gnasBank.models.BankAccount;
import nl.inholland.gnasBank.models.Role;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private UUID uuid;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private Double dayLimit;
    private Double transactionLimit;
    private List<Role> roles;
    private List<BankAccount> bankAccounts;
}

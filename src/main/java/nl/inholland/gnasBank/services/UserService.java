package nl.inholland.gnasBank.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import nl.inholland.gnasBank.models.User;
import nl.inholland.gnasBank.repositories.UserRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.UUID;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository repository;
//    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository repository) {
        this.repository = repository;
//        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAll(Integer page, Integer size, Boolean hasAccount) {
        PageRequest pageable = PageRequest.of(page, size);

        if (!hasAccount) {
            return repository.findAllByBankAccountsIsNull(pageable)
                    .getContent()
                    .stream()
                    .toList();
        }
        return repository.findAll(pageable)
                .getContent()
                .stream()
                .toList();
    }

    public User getById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"));
    }

    public User getByFirstAndLastName(String firstName, String lastName) {
        return repository.findUserByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(() -> new EntityNotFoundException("User with: " + firstName + " " + lastName + " not found"));
    }

    public User getByEmail(String email) {
        return repository.findUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + email + " not found"));
    }

    public User create(User user) {
        try {
            if (repository.findUserByEmail(user.getEmail()).isPresent()) {
                throw new RuntimeException("User with email " + user.getEmail() + " already exists");
            }

//            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return repository.save(user);
        } catch (DataAccessException | PersistenceException err) { //more specific error types
            throw new RuntimeException("Failed to create the user: " + err);
        }
    }

    public User update(UUID uuid, User user) {
        try {
            User existingUser = repository.findById(uuid)
                    .orElseThrow(() -> new EntityNotFoundException("User with UUID: " + uuid + " not found"));

            // Update the properties of the existing user
            existingUser.setEmail(user.getEmail());
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setPhone(user.getPhone());
            existingUser.setDayLimit(user.getDayLimit());
            existingUser.setTransactionLimit(user.getTransactionLimit());
            existingUser.setRoles(user.getRoles());
            existingUser.setBankAccounts(user.getBankAccounts());

            return repository.save(existingUser);
        } catch (DataAccessException | PersistenceException err) {
            throw new RuntimeException("Failed to update the user: " + err);
        }
    }

    public boolean delete(UUID uuid) {
        try {
            repository.deleteById(uuid);
            return true;
        } catch (DataAccessException | PersistenceException err) {
            throw new RuntimeException("Failed to delete the user: " + err);
        }
    }
}

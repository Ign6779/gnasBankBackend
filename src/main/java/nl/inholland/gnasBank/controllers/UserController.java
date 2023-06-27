package nl.inholland.gnasBank.controllers;

import lombok.extern.java.Log;
import nl.inholland.gnasBank.models.User;
import nl.inholland.gnasBank.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("users")
@Log
public class UserController {
    private final UserService service;

    public UserController(UserService userService) {
        this.service = userService;
    }

    @GetMapping
    public ResponseEntity getAll(@RequestParam(required = false, defaultValue = "0") Integer page,
                                 @RequestParam(required = false, defaultValue = "100") Integer size,
                                 @RequestParam(required = false, defaultValue = "true") Boolean hasAccount) {
        try {
            return ResponseEntity.ok(service.getAll(page, size, hasAccount));
        }
        catch (Exception e) {
            return ResponseEntity.status(400).body(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(service.getById(id));
        }
        catch (Exception e) {
            return ResponseEntity.status(404).body(e);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity getByEmail(@PathVariable String email) {
        try {
            return ResponseEntity.ok(service.getByEmail(email));
        }
        catch (Exception e) {
            return  ResponseEntity.status(404).body(e);
        }
    }

    @GetMapping("/search")
    public ResponseEntity getByUsername(@RequestParam String firstName,
                                        @RequestParam String lastName) {
        try {
            return ResponseEntity.ok(service.getByFirstAndLastName(firstName, lastName));
        }
        catch (Exception e) {
            return ResponseEntity.status(404).body(e);
        }
    }

    @PostMapping
    public ResponseEntity create(@RequestBody User user) {
        if (checkFields(user)) {
            try {
                return ResponseEntity.ok(service.create(user));
            }
            catch (Exception e) {
                return ResponseEntity.status(400).body(e);
            }
        }
        else {
            return ResponseEntity.status(400).body("User fields missing");
        }
    }

    private boolean checkFields(User user) {
        return user.getFirstName() != null && !user.getFirstName().isEmpty() &&
                user.getLastName() != null && !user.getLastName().isEmpty() &&
                user.getEmail() != null && !user.getEmail().isEmpty() &&
                user.getPhone() != null && !user.getPhone().isEmpty() &&
                user.getPassword() != null && !user.getPassword().isEmpty() &&
                user.getDayLimit() != null &&
                user.getTransactionLimit() != null &&
                user.getRoles() != null && !user.getRoles().isEmpty();
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable UUID id, @RequestBody User user) {
        if (checkFields(user)) {
            try {
                return ResponseEntity.ok(service.update(id, user));
            }
            catch (Exception e) {
                return ResponseEntity.status(404).body(e);
            }
        }
        else {
            return ResponseEntity.status(400).body("User fields missing");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(service.delete(id));
        }
        catch (Exception e) {
            return ResponseEntity.status(404).body(e);
        }
    }
}

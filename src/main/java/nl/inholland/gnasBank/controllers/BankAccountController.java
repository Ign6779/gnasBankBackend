package nl.inholland.gnasBank.controllers;

import lombok.extern.java.Log;
import nl.inholland.gnasBank.models.BankAccount;
import nl.inholland.gnasBank.models.dto.BankAccountDTO;
import nl.inholland.gnasBank.models.dto.UserDTO;
import nl.inholland.gnasBank.services.BankAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bankaccounts")
@Log
public class BankAccountController {
    private BankAccountService service;

    public BankAccountController(BankAccountService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity getAll(@RequestParam(required = false, defaultValue = "0") Integer page,
                                 @RequestParam(required = false, defaultValue = "100") Integer size) {
        try {
            return ResponseEntity.ok(service.getAll(page, size));
        }
        catch (Exception e) {
            return ResponseEntity.status(400).body(e);
        }
    }

    @GetMapping("/{iban}")
    public ResponseEntity getById(@PathVariable String iban) {
        try {
            return ResponseEntity.ok(service.getById(iban));
        }
        catch (Exception e) {
            return  ResponseEntity.status(404).body(e);
        }
    }

    @GetMapping("/search")
    public ResponseEntity getByUsername(@RequestParam String firstName,
                                        @RequestParam String lastName) {
        try {
            return ResponseEntity.ok(service.getByUsername(firstName, lastName));
        }
        catch (Exception e) {
            return ResponseEntity.status(404).body(e);
        }
    }

    @PostMapping
    public ResponseEntity create(@RequestBody BankAccountDTO dto) {
        if (checkFields(dto)) {
            try {
                return ResponseEntity.ok(service.create(dto));
            }
            catch (Exception e) {
                return ResponseEntity.status(400).body(e);
            }
        }
        else {
            return ResponseEntity.status(400).body("Bank account details missing");
        }
    }

    private boolean checkFields(BankAccountDTO dto) {
        return dto.getUserId() != null && !dto.getUserId().toString().isEmpty() &&
                (Double) dto.getAbsoluteLimit() != null &&
                (Double) dto.getBalance() != null &&
                dto.getType() != null && !dto.getType().toString().isEmpty();
    }

    @PutMapping("/{id}")
    public ResponseEntity update(@PathVariable String id, @RequestBody BankAccountDTO dto) {
        if (checkFields(dto)) {
            try {
                return ResponseEntity.ok(service.update(dto));
            }
            catch (Exception e) {
                return ResponseEntity.status(404).body(e);
            }
        }
        else {
            return ResponseEntity.status(400).body("Bank account details missing");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable String id) {
        try {
            return ResponseEntity.ok(service.delete(id));
        }
        catch (Exception e) {
            return  ResponseEntity.status(404).body(e);
        }
    }
}

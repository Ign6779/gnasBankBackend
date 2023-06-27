package nl.inholland.gnasBank.controllers;

import nl.inholland.gnasBank.models.BankAccount;
import nl.inholland.gnasBank.models.Transaction;
import nl.inholland.gnasBank.models.dto.TransactionDTO;
import nl.inholland.gnasBank.services.BankAccountService;
import nl.inholland.gnasBank.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("transactions")
public class TransactionController {
    private final TransactionService service;
    private final BankAccountService bankAccountService;

    public TransactionController(TransactionService service, BankAccountService bankAccountService) {
        this.service = service;
        this.bankAccountService = bankAccountService;
    }

    @GetMapping
    public ResponseEntity getAll(@RequestParam(required = false, defaultValue = "0") Integer page,
                                 @RequestParam(required = false, defaultValue = "100") Integer size,
                                 @RequestParam(required = false) String from,
                                 @RequestParam(required = false) String to,
                                 @RequestParam(required = false) String comparison,
                                 @RequestParam(required = false) Double amount,
                                 @RequestParam(required = false) LocalDateTime startDate,
                                 @RequestParam(required = false) LocalDateTime endDate) {
        try {
            if (from != null && to != null) {
                // Check if both from and to parameters are provided
                throw new IllegalArgumentException("Both from and to parameters cannot be provided simultaneously.");
            }

            if (from != null) {
                BankAccount accountFrom = bankAccountService.getById(from);
                List<Transaction> transactions;

                if (comparison != null && amount != null) {
                    // Apply amount comparison based on the provided comparison value
                    switch (comparison.toLowerCase()) {
                        case "smaller":
                            transactions = service.getByAccountFromAndAmountLessThan(accountFrom, amount+1);
                            break;
                        case "equal":
                            transactions = service.getByAccountFromAndAmountEquals(accountFrom, amount);
                            break;
                        case "greater":
                            transactions = service.getByAccountFromAndAmountGreaterThan(accountFrom, amount-1);
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid comparison value. Accepted values: smaller, equal, greater.");
                    }
                } else if (startDate != null && endDate != null) {
                    // Apply timestamp filtering based on the provided start and end dates
                    transactions = service.getByAccountFromAndTimeStampBetween(accountFrom, startDate, endDate);
                } else {
                    // Regular getByAccountFrom
                    transactions = service.getByAccountFrom(accountFrom, page, size);
                }

                return ResponseEntity.ok(transactions);
            }

            if (to != null) {
                BankAccount accountTo = bankAccountService.getById(to);
                List<Transaction> transactions;

                if (comparison != null && amount != null) {
                    // Apply amount comparison based on the provided comparison value
                    switch (comparison.toLowerCase()) {
                        case "smaller":
                            transactions = service.getByAccountToAndAmountLessThan(accountTo, amount+1);
                            break;
                        case "equal":
                            transactions = service.getByAccountToAndAmountEquals(accountTo, amount);
                            break;
                        case "greater":
                            transactions = service.getByAccountToAndAmountGreaterThan(accountTo, amount-1);
                            break;
                        default:
                            throw new IllegalArgumentException("Invalid comparison value. Accepted values: smaller, equal, greater.");
                    }
                } else if (startDate != null && endDate != null) {
                    // Apply timestamp filtering based on the provided start and end dates
                    transactions = service.getByAccountToAndTimeStampBetween(accountTo, startDate, endDate);
                } else {
                    // Regular getByAccountTo
                    transactions = service.getByAccountTo(accountTo, page, size);
                }

                return ResponseEntity.ok(transactions);
            }

            List<Transaction> transactions = service.getAll(page, size);
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable UUID id) {
        Transaction transaction = service.getById(id);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TransactionDTO dto) {
        try {
            return ResponseEntity.ok(service.create(dto));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(service.delete(id));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e);
        }
    }
}

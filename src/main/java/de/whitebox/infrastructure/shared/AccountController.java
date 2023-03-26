package de.whitebox.infrastructure.shared;

import de.whitebox.application.api.*;
import de.whitebox.application.api.Ledger.*;
import de.whitebox.application.bank.*;
import de.whitebox.domain.bank.*;
import de.whitebox.domain.shared.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController()
@RequestMapping("/account")

public class AccountController {

    private Ledger ledger;
    private Bank bank;

    public AccountController(Accounts accounts, Broker broker, Ledger ledger) {
        this.ledger = ledger;
        this.bank = new Bank(broker, accounts);
    }

    @PostMapping("/open")
    public UUID open(@RequestBody OpenRequest request) {
        return bank
                .open(new Customer(request.name(), request.surname()), request.deposit())
                .id();
    }

    @PostMapping("/debit")
    public List<Transaction> debit(@RequestBody DebitRequest request) {
        bank.deposit(request.id(), request.amount());
        return ledger.of(request.id());
    }

    @PostMapping("/credit")
    public List<Transaction> credit(@RequestBody CreditRequest request) {
        bank.credit(request.id(), request.amount());
        return ledger.of(request.id());
    }

    @GetMapping("/{id}")
    public List<Transaction> account(@PathVariable("id") UUID id) {
        return ledger.of(id);
    }

    record OpenRequest(String name, String surname, double deposit) {
    }

    record DebitRequest(UUID id, double amount) {
    }

    record CreditRequest(UUID id, double amount) {
    }
}

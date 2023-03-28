package de.whitebox.infrastructure.api;

import de.whitebox.application.api.*;
import de.whitebox.application.api.Query.*;
import de.whitebox.application.bank.*;
import de.whitebox.domain.bank.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.*;

import java.util.*;

import static org.springframework.http.ResponseEntity.*;

@RestController()
@RequestMapping("/account")

public class AccountController {

    private Query query;
    private UriComponentsBuilder uri;
    private Bank bank;

    public AccountController(Accounts accounts, Broker broker, Query query) {
        this.query = query;
        this.uri = UriComponentsBuilder.fromUriString("/account");
        this.bank = new Bank(broker, accounts);
    }

    @PostMapping("/open")
    public ResponseEntity<?> open(@RequestBody OpenRequest request) throws Account.RequiredDepositException {
        var id = bank
                .open(new Customer(request.name(), request.surname()), request.deposit())
                .id();
        var location = uri
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
        return created(location).build();
    }

    @PostMapping("/debit")
    public ResponseEntity<List<Transaction>> debit(@RequestBody DebitRequest request) throws Account.InsufficientDepositException {
        bank.deposit(request.id(), request.amount());
        return ResponseEntity.ok(query.of(request.id()));
    }

    @PostMapping("/credit")
    public ResponseEntity<List<Transaction>> credit(@RequestBody CreditRequest request) {
        bank.credit(request.id(), request.amount());
        return ResponseEntity.ok(query.of(request.id()));
    }

    @GetMapping("/{id}")
    public List<Transaction> account(@PathVariable("id") UUID id) {
        return query.of(id);
    }

    @GetMapping("/overdrafts")
    public List<Overdraft> overdrafts() {
        return query.overdrafts();
    }

    record OpenRequest(String name, String surname, double deposit) {
    }

    record DebitRequest(UUID id, double amount) {
    }

    record CreditRequest(UUID id, double amount) {
    }
}

package de.whitebox.infrastructure.api;

import de.whitebox.application.api.*;
import de.whitebox.application.api.Query.*;
import de.whitebox.application.bank.*;
import de.whitebox.application.bank.Bank.*;
import de.whitebox.domain.bank.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.*;

import java.util.*;

import static org.springframework.http.ResponseEntity.*;

/**
 * The controller for even command an action like: debit, deposit, open, etc,
 * or query accounts, account, etc.
 * General rule:
 * POST is always a command and will change the state;
 * GET is always a query, no side effect MUST be guaranteed;
 */
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
        return ok(query.of(request.id()));
    }

    @GetMapping("/{id}/debit/{amount}/allowance")
    public ResponseEntity<Allowance> allowance(@PathVariable("id") UUID id, @PathVariable("amount") Double amount) {
        return ok(bank.allowance(id, amount));
    }

    @PostMapping("/credit")
    public ResponseEntity<List<Transaction>> credit(@RequestBody CreditRequest request) {
        bank.credit(request.id(), request.amount());
        return ok(query.of(request.id()));
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

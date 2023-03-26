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

    private Accounts accounts;
    private Broker broker;
    private Ledger ledger;

    public AccountController(Accounts accounts, Broker broker, Ledger ledger) {
        this.accounts = accounts;
        this.broker = broker;
        this.ledger = ledger;
    }

    @PostMapping("/open")
    public List<Transaction> open(@RequestBody OpenRequest request) {
        var use = new Bank(broker, accounts);
        var customer = new Customer(request.name(), request.surname());
        use.open(customer, request.deposit());
        return ledger.of(customer.id());
    }

    @GetMapping("/{id}")
    public String account(@PathVariable("id") String id) {
       return "Hello "+id;
    }

    record OpenRequest(String name, String surname, double deposit) {
    }

    record DebitRequest(String id, double debit) {
    }

    record CreditRequest(String id, double credit) {
    }
}

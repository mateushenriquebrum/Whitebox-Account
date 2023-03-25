package de.whitebox.infrastructure.shared;

import de.whitebox.application.bank.*;
import de.whitebox.domain.bank.*;
import de.whitebox.domain.shared.*;
import org.springframework.web.bind.annotation.*;

@RestController
public class AccountController {

    private Accounts accounts;
    private Broker broker;

    public AccountController(Accounts accounts, Broker broker) {
        this.accounts = accounts;
        this.broker = broker;
    }

    @PostMapping("/open")
    public void open(@RequestBody OpenRequest request) {
        var use = new Bank(broker, accounts);
        var customer = new Customer(request.name(), request.surname());
        use.open(customer, request.deposit());
    }

    record OpenRequest(String name, String surname, double deposit) {
    }

    record DebitRequest(String id, double debit) {
    }

    record CreditRequest(String id, double credit) {
    }
}

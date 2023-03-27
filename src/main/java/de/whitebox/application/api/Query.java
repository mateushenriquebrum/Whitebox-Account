package de.whitebox.application.api;

import de.whitebox.domain.bank.*;
import org.springframework.stereotype.*;

import java.util.*;

import static java.time.LocalDateTime.now;

@Service
public class Query {

    private Registers register;
    private Overdrafts overdrafts;

    public Query(Registers register, Overdrafts overdrafts) {
        this.register = register;
        this.overdrafts = overdrafts;
    }

    public void add(Credited credited) {
        register.add(new Transaction(credited.account(), "IN", credited.amount(), credited.balance()));
    }

    public void add(Debited debited) {
        register.add(new Transaction(debited.account(),"OUT", debited.amount(), debited.balance()));
    }

    public void add(Opened opened) {
        register.add(new Transaction(opened.account(),"OPENED", opened.initial().deposit(), opened.initial().deposit()));
    }

    public void add(Overdrafted overdrafted) {
        overdrafts.insert(new Overdraft(overdrafted.account(), now()));
    }


    public void add(Balanced balance) {
        overdrafts.remove(balance.account());
    }

    public List<Transaction> of(UUID account) {
        return register.of(account);
    }

    public List<Overdraft> overdrafts() {
        return overdrafts.all();
    }

    public record Transaction(UUID account, String type, double amount, double balance) { }
}

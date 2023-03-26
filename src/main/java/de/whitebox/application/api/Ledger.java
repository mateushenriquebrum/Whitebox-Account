package de.whitebox.application.api;

import de.whitebox.domain.bank.*;
import io.swagger.v3.oas.annotations.servers.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class Ledger {

    private Transactions transactions;

    public Ledger(Transactions transactions) {
        this.transactions = transactions;
    }

    public void performed(Credit credit) {
        transactions.add(new Transaction(credit.customer().id(), "IN", credit.amount(), credit.balance()));
    }

    public void performed(Debit debit) {
        transactions.add(new Transaction(debit.customer().id(),"OUT", debit.amount(), debit.balance()));
    }

    public void opened(Opened opened) {
        transactions.add(new Transaction(opened.customer().id(),"OPENED", opened.initial().deposit(), opened.initial().deposit()));
    }

    public List<Transaction> of(UUID customer) {
        return transactions.of(customer);
    }

    public record Transaction(UUID customer, String type, double value, double balance) { }
}

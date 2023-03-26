package de.whitebox.application.api;

import de.whitebox.domain.bank.*;
import org.springframework.stereotype.*;

import java.util.*;

@Service
public class Ledger {

    private Transactions transactions;

    public Ledger(Transactions transactions) {
        this.transactions = transactions;
    }

    public void performed(Credit credit) {
        transactions.add(new Transaction(credit.account(), "IN", credit.amount(), credit.balance()));
    }

    public void performed(Debit debit) {
        transactions.add(new Transaction(debit.account(),"OUT", debit.amount(), debit.balance()));
    }

    public void opened(Opened opened) {
        transactions.add(new Transaction(opened.account(),"OPENED", opened.initial().deposit(), opened.initial().deposit()));
    }

    public List<Transaction> of(UUID account) {
        return transactions.of(account);
    }

    public record Transaction(UUID account, String type, double value, double balance) { }
}

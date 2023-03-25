package de.whitebox.domain.bank.entities;


import de.whitebox.domain.bank.events.*;
import de.whitebox.domain.bank.vos.*;
import de.whitebox.domain.shared.*;

import java.util.*;

import static java.util.Objects.requireNonNull;

/**
 * Account: an entity that control user debit and credit,
 * taking in account the credit line and deposit to properly
 * signalise overdraft and deny credit
 */
public class Account extends Entity {
    private double balance;
    private Customer customer;
    private boolean overdraft;
    private Granted credit;

    private Account(Customer customer, Opening deposit, Granted credit) {
        requireNonNull(customer);
        requireNonNull(deposit);
        requireMinimumAmount(deposit);
        this.balance = deposit.deposit();
        this.customer = customer;
        this.credit = credit;
        apply(new Opened(this.customer, deposit, credit));
    }

    /**
     * Event source used only, maybe some reflection and annotation can hide this
     */
    public Account(List<Event> events, UUID locked, UUID id) {
        super(events, locked, id);
    }

    public static Account open(Customer customer, Opening deposit, Granted line) {
        return new Account(customer, deposit, line);
    }

    public void credit(double amount) {
        var newBalance = this.balance + amount;
        apply(new Balance(this.customer, newBalance, newBalance < 0));
    }

    public void debit(double amount) {
        var newBalance = this.balance - amount;
        if (credit.notGrantedFor(newBalance)) {
            // There is a big change to release another event here
            // Should a credit line listen for it in order to offer a better package for ths customer?
            throw new IllegalArgumentException();
        }
        apply(new Balance(this.customer, newBalance, newBalance < 0));
    }

    private static void requireMinimumAmount(Opening amount) {
        if (!amount.isMinimumRequired()) {
            throw new IllegalArgumentException("Amount to open an account is not minimum required");
        }
    }

    @Override
    public void mutate(Event event) {
        //There is not yet a descent pattern match in java 19, so using instanceOf here
        if (event instanceof Balance balance) {
            this.balance = balance.amount();
            this.overdraft = balance.overdraft();
        } else if (event instanceof Opened opened) {
            this.balance = opened.initial().deposit();
            this.customer = opened.customer();
            this.credit = opened.credit();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account account)) return false;
        return Double.compare(account.balance, balance) == 0 && overdraft == account.overdraft && Objects.equals(customer, account.customer) && Objects.equals(credit, account.credit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance, customer, overdraft, credit);
    }

    @Override
    public String toString() {
        return "Account{" +
                "deposit=" + balance +
                ", customer=" + customer +
                ", overdraft=" + overdraft +
                ", line=" + credit +
                '}';
    }
}

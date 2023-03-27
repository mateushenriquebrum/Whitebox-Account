package de.whitebox.domain.bank;


import de.whitebox.domain.shared.*;

import java.util.*;

import static java.util.Objects.*;

/**
 * Account: an entity that control user debit and credit,
 * taking in account the credit line and deposit to properly
 * signalise overdraft and deny credit
 */
public class Account extends Entity {
    private double balance;
    private Customer customer;
    private boolean overdraft;
    private Granting credit;

    private Account(Customer customer, Opening deposit, Granting credit) {
        requireNonNull(customer);
        requireNonNull(deposit);
        requireMinimumDeposit(deposit);
        this.balance = deposit.deposit();
        this.customer = customer;
        this.credit = credit;
        apply(new Opened(this.id(), deposit, credit, customer));
    }

    /**
     * Event source used only, maybe some reflection and annotation can hide this
     */
    public Account(List<Event> events, UUID locked, UUID id) {
        super(events, locked, id);
    }

    public static Account open(Customer customer, Opening deposit, Granting line) {
        return new Account(customer, deposit, line);
    }

    public void credit(double amount) {
        var newBalance = this.balance + amount;
        apply(new Credited(this.id(), amount, newBalance));
        if(newBalance >= 0 && overdraft) {
            apply(new Balanced(this.id(), newBalance));
        }
    }

    public void debit(double amount) {
        var newBalance = this.balance - amount;
        if (credit.notGrantedFor(newBalance)) {
            // There is a big change to release another event here
            // Should a credit line listen for it in order to offer a better package for ths customer?
            throw new InsufficientDepositException();
        }
        apply(new Debited(this.id(), amount, newBalance));
        if(newBalance < 0 && !overdraft) {
            apply(new Overdrafted(this.id(), newBalance * -1));
        }
    }

    private static void requireMinimumDeposit(Opening amount) {
        if (!amount.isMinimumRequired()) {
            throw new IllegalArgumentException("Amount to open an account is not minimum required");
        }
    }

    @Override
    public void mutate(Event event) {
        //There is not yet a descent pattern match in java 19, so using instanceOf here
        if (event instanceof Credited e) {
            this.balance = e.balance();
        } else if (event instanceof Debited e) {
            this.balance = e.balance();
        } else if (event instanceof Opened e) {
            this.balance = e.initial().deposit();
            this.credit = e.credit();
            this.customer = e.customer();
        } else if (event instanceof Overdrafted e) {
            this.overdraft = true;
        } else if (event instanceof Balanced e) {
            this.overdraft = false;
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

    public static class InsufficientDepositException extends RuntimeException {}
}

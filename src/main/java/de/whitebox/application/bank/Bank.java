package de.whitebox.application.bank;

import de.whitebox.domain.bank.*;
import de.whitebox.domain.shared.*;

import java.util.*;

/**
 * It is an application service represent all known use case for Account entity
 * Also it is responsible to keep the consistence boundaries in application and system,
 * meaning that accounts MUST fail if the entity lock version does not match with
 * the current version in the underline persistence mechanism,
 * it should be acting as an Aggregator when needed.
 */
public record Bank(Broker publisher, Accounts accounts) implements Aggregator {
    /**
     * It commands to open an account applying the default credit and validate every parameter
     * and failing if not possible
     */
    public Account open(Customer customer, double initial) throws Account.RequiredDepositException {
        var opened = Account.open(customer, new Opening(initial), Granting.standardCreditLine());
        commit(opened);
        return opened;
    }

    /**
     * It commands to debit from an account applying all the checks and failing if not possible
     */
    public void deposit(UUID id, double amount) throws Account.InsufficientDepositException {
        var account = accounts.of(id);
        account.debit(amount);
        commit(account);
    }

    /**
     * It commands to credit to an account applying all the checks and failing if not possible
     */
    public void credit(UUID id, double amount) {
        var account = accounts.of(id);
        account.credit(amount);
        commit(account);
    }

    /**
     * Return the maximum debit allowed for this account
     */
    public Allowance allowance(UUID id, Double requested) {
        var account = accounts.of(id);
        return new Allowance(
                requested,
                account.balance(),
                account.granted(),
                account.balance() + account.granted()
                , account.isDebitAllowed(requested));
    }

    public record Allowance(double requested, double balance, double granted, double limit, boolean allowed) { }

    /**
     * This is pretty much a boilerplate that will happen with every command and
     * can mostly be addressed with some sort of annotation and reflection.
     * However, it is important highlight that the order here matters, if the
     * persistence fail it will be still possible to send the events eventually (as it was persisted),
     * although the opposite is not true, if publishing came first causing an overall inconsistent in system
     *
     * @param opened The Account to be persisted
     */
    private void commit(Account opened) {
        accounts.persist(opened); //first always
        publisher.publish(opened.changes());
    }
}

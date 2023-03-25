package de.whitebox.application.bank;

import de.whitebox.domain.bank.entities.*;
import de.whitebox.domain.bank.vos.*;
import de.whitebox.domain.bank.vos.Customer.*;
import de.whitebox.domain.shared.*;

/**
 * This service represent all the know use case for Account entity
 * Also it is responsible to keep the consistence boundaries in transaction
 * means that accounts MUST fail if the entity lock version does not match with
 * the underline persistence mechanism and should be acting as an Aggregator when needed.
 */
public record UseCases(Broker publisher, Accounts accounts) implements Aggregator{
    /**
     * It commands to open an account applying the default credit and validate every parameter
     * and failing if not possible
     */
    public void open(Customer customer, double initial) {
        var opened = Account.open(customer, new Opening(initial), Granted.standardCreditLine());
        commit(opened);
    }

    /**
     * It commands to debit from an account applying all the checks and failing if not possible
     */
    public void deposit(CustomerId id, double amount) {
        var account = accounts.of(id);
        account.debit(amount);
        commit(account);
    }

    /**
     * It commands to credit to an account applying all the checks and failing if not possible
     */
    public void credit(CustomerId id, double amount) {
        var account = accounts.of(id);
        account.credit(amount);
        commit(account);
    }

    /**
     * This is pretty much a boilerplate that will happen with every command and
     * can mostly be addressed with some sort of annotation and reflection.
     * However, it is important highlight that the order here matters, if the
     * persistence fail it will be still possible to send the events eventually (as it was persisted),
     * although the opposite is not true, if publishing came first causing an overall inconsistent in system
     * @param opened The Account to be persisted
     */
    private void commit(Account opened) {
        accounts.persist(opened.changes(), opened.locked(), opened.id()); //first always
        publisher.publish(opened.changes());
    }
}

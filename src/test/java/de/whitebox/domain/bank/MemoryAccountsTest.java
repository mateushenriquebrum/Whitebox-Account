package de.whitebox.domain.bank;

import de.whitebox.infrastructure.bank.*;
import de.whitebox.infrastructure.bank.MemoryAccounts.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class MemoryAccountsTest {

    @Test
    void shouldPersistEntity() throws Account.RequiredDepositException {
        var accounts = new MemoryAccounts();
        var account = Account.open(new Customer("", ""), new Opening(100), new Granting(100));
        accounts.persist(account);
        assertEquals(accounts.of(account.id()), account);
    }

    @Test
    void shouldThrowOptimisticLockException() throws Account.InsufficientDepositException, Account.RequiredDepositException {
        var accounts = new MemoryAccounts();
        var created = Account.open(new Customer("", ""), new Opening(100), new Granting(100));
        accounts.persist(created.changes(), created.locked(), created.id());

        var first = accounts.of(created.id());
        var second = accounts.of(created.id());

        first.debit(10); //concurrency
        second.debit(10);//concurrency

        accounts.persist(first); //first to commit wins
        assertThrows(OptimisticLockException.class, () ->
                accounts.persist(second));
    }
}

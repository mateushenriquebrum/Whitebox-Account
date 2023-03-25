package de.whitebox.domain;

import de.whitebox.domain.bank.*;
import de.whitebox.infrastructure.shared.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class MemoryAccountsTest {

    @Test
    void shouldPersistEntity() {
        var accounts = new MemoryAccounts();
        var account = Account.open(new Customer("", ""), new Opening(100), new Granted(100));
        accounts.persist(account);
        assertEquals(accounts.of(account.id()), account);
    }

    @Test
    void shouldThrowOptimisticLockException() {
        var accounts = new MemoryAccounts();
        var created = Account.open(new Customer("", ""), new Opening(100), new Granted(100));
        accounts.persist(created.changes(), created.locked(), created.id());

        var first = accounts.of(created.id());
        var second = accounts.of(created.id());

        first.debit(10); //concurrency
        second.debit(10);//concurrency

        accounts.persist(first);
        assertThrows(RuntimeException.class, () ->
                accounts.persist(second));
    }
}

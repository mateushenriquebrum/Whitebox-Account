package de.whitebox.domain;

import de.whitebox.domain.bank.entities.*;
import de.whitebox.domain.bank.events.*;
import de.whitebox.domain.bank.vos.*;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountTest {

    Customer customer = new Customer("", "");
    Opening deposit = new Opening(100.00);
    Granted credit = new Granted(200.00);

    @Test
    void shouldAlwaysBeOpenedWithDeposit() {
        var good = new Opening(25.00);
        var bad = new Opening(24.99);
        assertDoesNotThrow(() -> Account.open(customer, good, credit));
        assertThrows(IllegalArgumentException.class, () -> Account.open(customer, bad, credit));
        assertTrue(Account.open(customer, good, credit).changes().contains(new Opened(customer, good, credit)));
    }

    @Test
    void shouldCreditIncreaseBalance() {
        var account = create();
        account.credit(10.00);
        assertTrue(account.changes().contains(new Balance(customer, 110.00, false)));
    }

    @Test
    void shouldDebitDecreaseBalance() {
        var account = create();
        account.debit(10.00);
        assertTrue(account.changes().contains(new Balance(customer, 90.00, false)));
    }

    @Test
    void shouldDebitOverdraftAnAccount() {
        var account = create();
        account.debit(101.00);
        assertTrue(account.changes().contains(new Balance(customer, -1.00, true)));
    }

    @Test
    void shouldCreditNotOverdraftAnAccount() {
        var account = create();
        account.debit(101.00);
        account.credit(1.00);
        assertTrue(account.changes().contains(new Balance(customer, 0.00, false)));
    }

    @Test
    void shouldNotAllowDebitIfCreditLineWasUsed() {
        var account = create();
        assertThrows(IllegalArgumentException.class, () -> account.debit(301.00));
    }

    private Account create() {
        return Account.open(customer, deposit, credit);
    }
}

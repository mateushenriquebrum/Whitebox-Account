package de.whitebox.domain;

import de.whitebox.domain.bank.*;
import de.whitebox.domain.bank.Account.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    Customer customer = new Customer("", "");
    Opening deposit = new Opening(100);
    Granted credit = new Granted(200);

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
        assertTrue(account.changes().contains(new Credit(customer, 10, 110, false)));
    }

    @Test
    void shouldDebitDecreaseBalance() {
        var account = create();
        account.debit(10);
        assertTrue(account.changes().contains(new Debit(customer, 10, 90, false)));
    }

    @Test
    void shouldDebitOverdraftAnAccount() {
        var account = create();
        account.debit(101);
        assertTrue(account.changes().contains(new Debit(customer, 101, -1, true)));
    }

    @Test
    void shouldCreditReturnToNotOverdraftAnAccount() {
        var account = create();
        account.debit(101);
        account.credit(1);
        assertTrue(account.changes().contains(new Credit(customer, 1, 0, false)));
    }

    @Test
    void shouldNotAllowDebitIfCreditLineWasUsed() {
        var account = create();
        assertThrows(InsufficientDepositException.class, () -> account.debit(301));
    }

    private Account create() {
        return Account.open(customer, deposit, credit);
    }
}

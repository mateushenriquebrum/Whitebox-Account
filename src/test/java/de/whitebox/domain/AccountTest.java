package de.whitebox.domain;

import de.whitebox.domain.bank.*;
import de.whitebox.domain.bank.Account.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    Customer customer = new Customer("", "");
    Opening deposit = new Opening(100);
    Granting credit = new Granting(200);

    @Test
    void shouldAlwaysBeOpenedWithDeposit() {
        var good = new Opening(25.00);
        var bad = new Opening(24.99);
        assertDoesNotThrow(() -> Account.open(customer, good, credit));
        assertThrows(IllegalArgumentException.class, () -> Account.open(customer, bad, credit));
        var open = Account.open(customer, good, credit);
        assertTrue(open.changes().contains(new Opened(open.id(), good, credit, customer)));
    }

    @Test
    void shouldCreditIncreaseBalance() {
        var account = create();
        account.credit(10.00);
        assertTrue(account.changes().contains(new Credited(account.id(), 10, 110)));
    }

    @Test
    void shouldDebitDecreaseBalance() {
        var account = create();
        account.debit(10);
        assertTrue(account.changes().contains(new Debited(account.id(), 10, 90)));
    }

    @Test
    void shouldDebitOverdraftAnAccount() {
        var account = create();
        account.debit(101);
        assertTrue(account.changes().contains(new Debited(account.id(), 101, -1)));
    }

    @Test
    void shouldCreditReturnToNotOverdraftAnAccount() {
        var account = create();
        account.debit(101);
        account.credit(1);
        assertTrue(account.changes().contains(new Credited(account.id(), 1, 0)));
    }

    @Test
    void shouldNotAllowDebitIfCreditLineWasUsed() {
        var account = create();
        assertThrows(InsufficientDepositException.class, () -> account.debit(301));
    }

    @Test
    void shouldOverdraft() {
        var account = create();
        account.debit(101);
        assertTrue(account.changes().contains(new Overdrafted(account.id(), 1)));
    }

    @Test
    void shouldRecoverBalance() {
        var account = create();
        account.debit(101);
        account.credit(1);
        assertTrue(account.changes().contains(new Balanced(account.id(), 0)));
    }

    private Account create() {
        return Account.open(customer, deposit, credit);
    }
}

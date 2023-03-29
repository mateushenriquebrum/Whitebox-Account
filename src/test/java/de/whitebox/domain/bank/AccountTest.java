package de.whitebox.domain.bank;

import de.whitebox.domain.bank.Account.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    Customer customer = new Customer("", "");
    Opening deposit = new Opening(100);
    Granting credit = new Granting(200);

    Double INITIAL_DEPOSIT_PLUS_GRANTING = 300D;

    @Test
    void shouldAlwaysBeOpenedWithDeposit() throws RequiredDepositException {
        var good = new Opening(25.00);
        var bad = new Opening(24.99);
        assertDoesNotThrow(() -> Account.open(customer, good, credit));
        assertThrows(RequiredDepositException.class, () -> Account.open(customer, bad, credit));
        var open = Account.open(customer, good, credit);
        assertTrue(open.changes().contains(new Opened(open.id(), good, credit, customer)));
    }

    @Test
    void shouldCreditIncreaseBalance() throws RequiredDepositException {
        var account = create();
        account.credit(10.00);
        assertTrue(account.changes().contains(new Credited(account.id(), 10, 110)));
    }

    @Test
    void shouldDebitDecreaseBalance() throws InsufficientDepositException, RequiredDepositException {
        var account = create();
        account.debit(10);
        assertTrue(account.changes().contains(new Debited(account.id(), 10, 90)));
    }

    @Test
    void shouldDebitOverdraftAnAccount() throws InsufficientDepositException, RequiredDepositException {
        var account = create();
        account.debit(101);
        assertTrue(account.changes().contains(new Debited(account.id(), 101, -1)));
    }

    @Test
    void shouldCreditReturnToNotOverdraftAnAccount() throws InsufficientDepositException, RequiredDepositException {
        var account = create();
        account.debit(101);
        account.credit(1);
        assertTrue(account.changes().contains(new Credited(account.id(), 1, 0)));
    }

    @Test
    void shouldNotAllowDebitIfCreditLineWasUsed() throws RequiredDepositException {
        var account = create();
        assertThrows(InsufficientDepositException.class, () -> account.debit(301));
    }

    @Test
    void shouldOverdraft() throws InsufficientDepositException, RequiredDepositException {
        var account = create();
        account.debit(101);
        assertTrue(account.changes().contains(new Overdrafted(account.id())));
    }

    @Test
    void shouldRecoverBalance() throws InsufficientDepositException, RequiredDepositException {
        var account = create();
        account.debit(101);
        account.credit(1);
        assertTrue(account.changes().contains(new Balanced(account.id(), 0)));
    }

    @Test
    void shouldValidateDebit() throws InsufficientDepositException, RequiredDepositException {
        var account = create();
        assertTrue(account.isDebitAllowed(INITIAL_DEPOSIT_PLUS_GRANTING));
        assertFalse(account.isDebitAllowed(INITIAL_DEPOSIT_PLUS_GRANTING + 1));
    }

    private Account create() throws RequiredDepositException {
        return Account.open(customer, deposit, credit);
    }
}

package de.whitebox.application.bank;

import de.whitebox.domain.bank.*;

import java.util.*;

/**
 * Accounts repository
 */
public interface Accounts {
    void persist(Account account);

    Account of(UUID id);
}

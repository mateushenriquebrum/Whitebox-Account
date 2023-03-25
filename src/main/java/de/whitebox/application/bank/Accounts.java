package de.whitebox.application.bank;

import de.whitebox.domain.bank.entities.*;
import de.whitebox.domain.bank.vos.*;
import de.whitebox.domain.shared.*;

import java.util.*;

public interface Accounts {
    void persist(List<Event> events, UUID locked, UUID id);

    Account of(Customer.CustomerId id);
}

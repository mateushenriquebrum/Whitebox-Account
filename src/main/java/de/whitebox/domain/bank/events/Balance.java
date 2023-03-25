package de.whitebox.domain.bank.events;

import de.whitebox.domain.bank.vos.*;
import de.whitebox.domain.shared.*;

/**
 * A Domain Event, also used by event source
 * @param customer Customer whose balance was changed.
 * @param amount New amount.
 * @param overdraft Overdraft flag, can be replaced by its own event if needed.
 */
public record Balance(Customer customer, double amount, boolean overdraft) implements Event {
}

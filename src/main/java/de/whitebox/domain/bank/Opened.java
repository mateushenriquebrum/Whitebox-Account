package de.whitebox.domain.bank;

import de.whitebox.domain.shared.*;

/**
 * A Domain Event, also used by event source
 *
 * @param customer Customer whose balance was changed.
 * @param initial  Initial deposit, constrained by Opening policy.
 * @param credit   Granted credit when open a new account.
 */
public record Opened(Customer customer, Opening initial, Granted credit) implements Event {
}

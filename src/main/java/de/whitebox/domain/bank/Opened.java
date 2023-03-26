package de.whitebox.domain.bank;

import de.whitebox.domain.shared.*;

import java.util.*;

/**
 * A Domain Event, also used by event source
 *
 * @param account  Account identification.
 * @param initial  Initial deposit, constrained by Opening policy.
 * @param credit   Granted credit when open a new account.
 */
public record Opened(UUID account, Opening initial, Granted credit) implements Event {
}

package de.whitebox.domain.bank;

import de.whitebox.domain.shared.*;

import java.util.*;

/**
 * Event used in event source mechanism but also as domain events if needed.
 * Used to identify when a branch new account was opened, it happens just once in the whole life cycle.
 * @param account Account identification.
 * @param initial Initial deposit, constrained by Opening policy.
 * @param credit  Granted credit when open a new account.
 */
public record Opened(UUID account, Opening initial, Granting credit, Customer customer) implements Event {
}

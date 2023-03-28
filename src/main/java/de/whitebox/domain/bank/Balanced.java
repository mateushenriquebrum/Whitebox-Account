package de.whitebox.domain.bank;

import de.whitebox.domain.shared.*;

import java.util.*;

/**
 * Event used in event source mechanism but also as domain events if needed.
 * It indicates an account recovery, for example, such account got overdraft
 * then a deposit was made covering fully the due amount, then this event will be emitted.
 * Read: The Account has balance again.
 * @param account Account id
 * @param balance Current balance after fact
 */
public record Balanced(UUID account, double balance) implements Event {
}

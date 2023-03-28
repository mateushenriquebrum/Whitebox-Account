package de.whitebox.domain.bank;

import de.whitebox.domain.shared.*;

import java.util.*;


/**
 * Event used in event source mechanism but also as domain events if needed.
 * Used to identify a credit in an account, the opposite operation of Debited.
 * @param account Account id
 * @param amount Amount credited
 * @param balance Current balance after fact
 */
public record Credited(UUID account, double amount, double balance) implements Event {
}

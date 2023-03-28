package de.whitebox.domain.bank;

import de.whitebox.domain.shared.*;

import java.util.*;


/**
 * Event used in event source mechanism but also as domain events if needed.
 * It indicates an account was overdrafted (went bellow zero).
 * Read: The Account has balance again.
 * @param account Account id
 */
public record Overdrafted(UUID account) implements Event {
}

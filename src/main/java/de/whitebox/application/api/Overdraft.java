package de.whitebox.application.api;

import java.time.*;
import java.util.*;

/**
 * It is a value object used to store overdraft status, used by API
 * @param account Account who overdraft
 * @param at Moment of overdraft
 */
public record Overdraft(UUID account, LocalDateTime at) {
}

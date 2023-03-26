package de.whitebox.domain.bank;

import de.whitebox.domain.shared.*;

import java.util.*;

public record Overdraft(UUID customer, double amount) implements Event {
}

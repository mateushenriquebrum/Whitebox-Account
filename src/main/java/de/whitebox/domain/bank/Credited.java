package de.whitebox.domain.bank;

import de.whitebox.domain.shared.*;

import java.util.*;

public record Credited(UUID account, double amount, double balance) implements Event {
}

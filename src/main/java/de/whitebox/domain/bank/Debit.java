package de.whitebox.domain.bank;

import de.whitebox.domain.shared.*;

import java.util.*;

public record Debit(UUID account, double amount, double balance, boolean overdraft) implements Event {
}

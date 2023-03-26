package de.whitebox.domain.bank;

import de.whitebox.domain.shared.*;

public record Credit(Customer customer, double amount, double balance, boolean overdraft) implements Event {
}

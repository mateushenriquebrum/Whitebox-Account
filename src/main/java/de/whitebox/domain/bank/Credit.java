package de.whitebox.domain.bank;

import de.whitebox.domain.shared.*;

import java.util.*;

public record Credit(UUID account, double amount, double balance) implements Event {
}

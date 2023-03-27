package de.whitebox.domain.bank;

import de.whitebox.domain.shared.*;

import java.util.*;

public record Balanced(UUID account, double balance) implements Event {
}

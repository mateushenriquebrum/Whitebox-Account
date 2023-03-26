package de.whitebox.domain.bank;

import de.whitebox.domain.shared.*;

import java.util.*;

public record Balance(UUID account, double balance) implements Event {
}

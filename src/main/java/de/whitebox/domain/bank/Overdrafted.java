package de.whitebox.domain.bank;

import de.whitebox.domain.shared.*;

import java.util.*;

public record Overdrafted(UUID account) implements Event {
}

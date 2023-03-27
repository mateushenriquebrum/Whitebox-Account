package de.whitebox.application.api;

import java.time.*;
import java.util.*;

public record Overdraft(UUID account, double amount, LocalDateTime at) {
}
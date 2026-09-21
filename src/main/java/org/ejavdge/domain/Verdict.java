package org.ejavdge.domain;

import org.ejavdge.error.InvariantViolation;

@FunctionalInterface
public interface Verdict {
    boolean ok() throws InvariantViolation;
}

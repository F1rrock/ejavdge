package org.ejavdge.app;

import org.ejavdge.error.InvariantViolation;

@FunctionalInterface
public interface App {
    void run() throws InvariantViolation;
}

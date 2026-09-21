package org.ejavdge.file;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

@FunctionalInterface
public interface Program {
    String outcomeOf(final Text i) throws InvariantViolation;
}

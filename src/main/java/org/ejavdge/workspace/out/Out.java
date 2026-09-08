package org.ejavdge.workspace.out;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

@FunctionalInterface
public interface Out {
    void write(final Text t) throws InvariantViolation;
}

package org.ejavdge.workspace.env;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public interface EnvVariable {
    String value() throws InvariantViolation;
    void assignWith(final Text v) throws InvariantViolation;
}

package org.ejavdge.scalar.num;

import org.ejavdge.error.InvariantViolation;

public final class NumAbout implements Num {
    private final String subject;
    private final Num origin;

    public NumAbout(final String s, final Num n) {
        this.subject = s;
        this.origin = n;
    }

    @Override
    public int value() throws InvariantViolation {
        try {
            return this.origin.value();
        } catch (final InvariantViolation err) {
            throw new InvariantViolation(
                "problem with %s: %s".formatted(
                    this.subject,
                    err.getMessage()
                )
            );
        }
    }
}
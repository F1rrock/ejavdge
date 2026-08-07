package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

public final class TextAbout implements Text {
    private final String subject;
    private final Text origin;

    public TextAbout(final String s, final Text t) {
        this.subject = s;
        this.origin = t;
    }

    @Override
    public String content() throws InvariantViolation {
        try {
            return this.origin.content();
        } catch (final InvariantViolation err) {
            throw new InvariantViolation(
                "Problem with %s: %s".formatted(
                    this.subject,
                    err.getMessage()
                )
            );
        }
    }
}
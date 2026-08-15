package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;

public final class BytesAbout implements Bytes {
    private final String subject;
    private final Bytes origin;

    public BytesAbout(final String s, final Bytes bs) {
        this.subject = s;
        this.origin = bs;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        try {
            return this.origin.content();
        } catch (final InvariantViolation err) {
            throw new InvariantViolation(
                "problem with %s\n".formatted(this.subject),
                err
            );
        }
    }
}
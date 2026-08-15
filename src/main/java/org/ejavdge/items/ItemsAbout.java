package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;

import java.util.List;

public final class ItemsAbout<T> implements Items<T> {
    private final String subject;
    private final Items<T> origin;

    public ItemsAbout(final String s, final Items<T> xs) {
        this.subject = s;
        this.origin = xs;
    }

    @Override
    public List<T> contents() throws InvariantViolation {
        try {
            return this.origin.contents();
        } catch (final InvariantViolation err) {
            throw new InvariantViolation(
                "problem with %s\n".formatted(this.subject),
                err
            );
        }
    }
}
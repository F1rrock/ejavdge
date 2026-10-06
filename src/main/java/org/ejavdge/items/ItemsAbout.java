package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;

import java.util.List;

/**
 * An {@link Items} decorator that attaches a descriptive subject to a
 * collection, used to enrich error messages.
 * <p>
 * This class wraps another {@link Items} instance along with a short textual
 * subject (such as {@code "problem's file references"}). When the underlying
 * collection's {@link Items#contents()} method throws an
 * {@link InvariantViolation}, this decorator catches it and rethrows a new
 * {@code InvariantViolation} whose message identifies the subject, with the
 * original exception attached as the cause.
 * <p>
 * This makes it easier to diagnose failures when several collections are being
 * materialized, by pointing to which collection failed.
 *
 * @param <T> the type of elements in the collection
 */
public final class ItemsAbout<T> implements Items<T> {

    /**
     * A short description of the subject represented by this collection.
     */
    private final String subject;

    /**
     * The underlying collection whose contents are decorated with the subject.
     */
    private final Items<T> origin;

    /**
     * Creates a new collection decorator with the given subject and origin.
     *
     * @param s  the descriptive subject used in error messages
     * @param xs the underlying collection
     */
    public ItemsAbout(final String s, final Items<T> xs) {
        this.subject = s;
        this.origin = xs;
    }

    /**
     * Returns the contents of the underlying collection, enriching any
     * {@link InvariantViolation} with the configured subject.
     * <p>
     * If {@link Items#contents()} on the underlying collection succeeds, its
     * result is returned unchanged. If it throws an {@code InvariantViolation},
     * that exception is wrapped in a new {@code InvariantViolation} whose
     * message includes the subject, with the original exception as the cause.
     *
     * @return the list of elements from the underlying collection
     * @throws InvariantViolation if the underlying collection fails to
     *         materialize its contents, with the subject included in the
     *         message
     */
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

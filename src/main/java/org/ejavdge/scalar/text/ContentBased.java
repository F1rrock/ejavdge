package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

/**
 * A {@link Text} decorator that implements value-based equality and hashing
 * based on the text's content rather than its object identity.
 * <p>
 * By default, two distinct {@code Text} implementations are not considered
 * equal even if they produce the same string content. This class changes that
 * behavior: a {@code ContentBased} instance is equal to another
 * {@code ContentBased} instance if and only if their materialized content
 * strings are equal, and its hash code is derived from the content string as
 * well.
 * <p>
 * This is useful whenever text values need to be compared by their content —
 * for example, when filtering out empty or duplicate lines from a collection
 * using predicates like {@code equals}. Because the content may involve
 * expensive computation and can throw an {@link InvariantViolation}, equality
 * and hashing are best-effort operations that propagate any such failure.
 * <p>
 * Note that equality is only defined between two {@code ContentBased} instances;
 * comparing a {@code ContentBased} to any other {@code Text} implementation
 * returns {@code false}, even if their contents happen to match. This preserves
 * symmetry and prevents unexpected interactions with other {@code Text} types.
 */
public final class ContentBased implements Text {

    /**
     * The underlying text whose content drives equality and hashing.
     */
    private final Text origin;

    /**
     * Creates a content-based text from the given string.
     *
     * @param s the string to wrap
     */
    public ContentBased(final String s) {
        this(new Text.Of(s));
    }

    /**
     * Creates a content-based text from the given text.
     *
     * @param t the text to wrap
     */
    public ContentBased(final Text t) {
        this.origin = t;
    }

    /**
     * Compares this content-based text to another object for equality.
     * <p>
     * Two {@code ContentBased} instances are considered equal if their
     * materialized content strings are equal. If the other object is not a
     * {@code ContentBased} instance, this method returns {@code false}.
     * <p>
     * Because materializing the content may fail with an
     * {@link InvariantViolation}, this method is not guaranteed to return
     * normally for arbitrary inputs.
     *
     * @param other the object to compare with
     * @return {@code true} if the other object is a {@code ContentBased} with
     *         the same content, {@code false} otherwise
     */
    @Override
    public boolean equals(final Object other) {
        if (other instanceof ContentBased t) {
            return this.content().equals(t.content());
        }
        return false;
    }

    /**
     * Returns a hash code derived from the materialized content string.
     * <p>
     * The hash code is consistent with {@link #equals(Object)}: two equal
     * {@code ContentBased} instances will produce the same hash code. As with
     * {@code equals}, this method may fail with an
     * {@link InvariantViolation} if the content cannot be materialized.
     *
     * @return the hash code of the content string
     */
    @Override
    public int hashCode() {
        return this.content().hashCode();
    }

    /**
     * Returns the content of the underlying text.
     *
     * @return the text content as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}

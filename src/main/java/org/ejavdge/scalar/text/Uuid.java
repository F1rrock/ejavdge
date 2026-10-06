package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

import java.util.UUID;

/**
 * A {@link Text} that represents a Universally Unique Identifier (UUID).
 * <p>
 * This class implements {@link Text} and provides a convenient way to work with
 * UUIDs as text values. It can either generate a new random UUID or wrap an
 * existing UUID string. When {@link #content()} is called, it returns the
 * string representation of the UUID.
 * <p>
 * This is useful for creating unique identifiers, such as for form fields,
 * request parameters, or temporary file names, and for treating them uniformly
 * with other text values in the application.
 */
public final class Uuid implements Text {

    /**
     * The underlying text representing the UUID.
     */
    private final Text origin;

    /**
     * Creates a new UUID by generating a random UUID and wrapping its string
     * representation.
     * <p>
     * The UUID is generated using {@link UUID#randomUUID()}.
     */
    public Uuid() {
        this(UUID.randomUUID().toString());
    }

    /**
     * Creates a new UUID from the given string.
     * <p>
     * The string is not validated as a UUID; it is simply wrapped as text.
     *
     * @param s the string representation of the UUID
     */
    public Uuid(final String s) {
        this(new Text.Of(s));
    }

    /**
     * Creates a new UUID from the given text.
     * <p>
     * The text is not validated as a UUID; it is simply wrapped.
     *
     * @param t the text representing the UUID
     */
    public Uuid(final Text t) {
        this.origin = t;
    }

    /**
     * Returns the string representation of the UUID.
     *
     * @return the UUID as a string
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}

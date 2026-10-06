package org.ejavdge.scalar.text;

/**
 * A {@link Text} that always represents an empty string.
 * <p>
 * This class provides a constant, immutable text value whose content is always
 * the empty string. It is useful as a neutral element in text concatenation,
 * as a default separator, or wherever an empty text is required without
 * allocating a new {@link Text.Of} instance each time.
 * <p>
 * Because the content is fixed and trivial, {@link #content()} never throws an
 * {@link org.ejavdge.error.InvariantViolation} and is effectively a no-op.
 */
public final class Empty implements Text {

    /**
     * The underlying text, which is always the empty string.
     */
    private final Text origin;

    /**
     * Creates a new empty text.
     */
    public Empty() {
        this.origin = new Text.Of("");
    }

    /**
     * Returns the empty string.
     *
     * @return the empty string
     */
    @Override
    public String content() {
        return this.origin.content();
    }
}

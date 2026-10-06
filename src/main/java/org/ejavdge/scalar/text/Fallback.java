package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

/**
 * A {@link Text} that provides fallback content when the primary content cannot
 * be obtained.
 * <p>
 * This class wraps two {@link Text} instances: a primary source and a fallback
 * source. When {@link #content()} is called, it first attempts to obtain the
 * content from the primary source. If that attempt succeeds, the resulting
 * string is returned. If the primary source throws an
 * {@link InvariantViolation}, the exception is caught, and the content is
 * instead obtained from the fallback source.
 * <p>
 * This is useful for optional textual values where a default or alternative
 * text should be used when the primary source is unavailable. For example, a
 * problem description might be extracted from a page, and if the extraction
 * fails, a generic message such as "There is no such problem." can be shown
 * instead.
 */
public final class Fallback implements Text {

    /**
     * The primary source of the text content.
     */
    private final Text origin;

    /**
     * The fallback source, used if the primary source fails.
     */
    private final Text then;

    /**
     * Creates a fallback text from the given primary and fallback sources.
     *
     * @param t the primary source of the text content
     * @param f the fallback source used if the primary source fails
     */
    public Fallback(final Text t, final Text f) {
        this.origin = t;
        this.then = f;
    }

    /**
     * Returns the text content, preferring the primary source and falling back
     * to the secondary source on failure.
     * <p>
     * The method first attempts to obtain the content from the primary source
     * via {@link Text#content()}. If that call throws an
     * {@link InvariantViolation}, the method instead obtains the content from
     * the fallback source. Any exception thrown by the fallback source is
     * propagated to the caller.
     *
     * @return the text content from the primary source if available, otherwise
     *         from the fallback source
     * @throws InvariantViolation if the fallback source fails to produce
     *         content
     */
    @Override
    public String content() throws InvariantViolation {
        try {
            return this.origin.content();
        } catch (final InvariantViolation e) {
            return this.then.content();
        }
    }
}

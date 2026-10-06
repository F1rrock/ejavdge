package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;

import java.util.Locale;

/**
 * A {@link Text} decorator that converts its content to lowercase.
 * <p>
 * This class wraps another {@link Text} instance and, when {@link #content()}
 * is called, returns the underlying content converted to lowercase. The
 * conversion uses {@link Locale#ROOT}, which is the locale-independent
 * "root" locale, ensuring predictable results regardless of the system's
 * default locale.
 * <p>
 * Using {@code Locale.ROOT} avoids the well-known issues associated with
 * locale-sensitive case conversion, such as the Turkish dotless {@code i},
 * where {@code "I".toLowerCase()} would produce {@code "ı"} instead of
 * {@code "i"} in the Turkish locale. This makes the transformation suitable
 * for programmatic use, such as case-insensitive comparisons or normalization
 * of extracted text.
 */
public final class Lowers implements Text {

    /**
     * The underlying text whose content is converted to lowercase.
     */
    private final Text origin;

    /**
     * Creates a lowercase view over the given text.
     *
     * @param t the text to convert to lowercase
     */
    public Lowers(final Text t) {
        this.origin = t;
    }

    /**
     * Returns the content of the underlying text converted to lowercase.
     * <p>
     * The conversion is performed using {@link Locale#ROOT}, so the result is
     * independent of the default locale of the running JVM.
     *
     * @return the lowercase content of the underlying text
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the content
     */
    @Override
    public String content() throws InvariantViolation {
        return this.origin.content().toLowerCase(Locale.ROOT);
    }
}

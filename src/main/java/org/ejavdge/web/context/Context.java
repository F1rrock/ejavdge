package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.web.media.Media;

/**
 * A functional interface representing a web context that can imprint its data
 * onto a given {@link Media} object.
 * <p>
 * Implementations of this interface encapsulate a specific aspect of a web
 * request or response — such as headers, cookies, query parameters, or body
 * parts — and provide a way to apply that data to a {@code Media} instance,
 * producing a result of some type {@code T}. This is typically used to
 * construct web requests or to extract values from responses in a composable
 * manner.
 * <p>
 * This is a functional interface whose functional method is
 * {@link #imprint(Media)}.
 */
@FunctionalInterface
public interface Context {

    /**
     * Imprints this context onto the given media and returns the resulting
     * value.
     * <p>
     * The exact behavior depends on the implementation and the type of media.
     * Typically, the context provides some data (e.g., a header value, a
     * cookie, a form field) that is combined with the media to produce a value
     * of type {@code T}. For example, a context representing an HTTP header
     * might produce a {@code Header} object when imprinted onto a media that
     * supports headers.
     *
     * @param <T> the type of the result produced by the imprint operation
     * @param m   the media to imprint this context onto
     * @return the result of imprinting this context onto the media
     * @throws InvariantViolation if an invariant is violated during the imprint
     *         operation, for example if the context cannot be applied to the
     *         given media
     */
    <T> T imprint(final Media<T> m) throws InvariantViolation;
}

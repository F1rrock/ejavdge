package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;

import java.util.function.Function;

/**
 * A monadic bind operation for {@link Bytes}.
 * <p>
 * This class implements {@link Bytes} and represents a deferred transformation
 * of byte content. It holds an original {@code Bytes} instance and a function
 * that takes the original byte array and produces a new {@code Bytes} instance.
 * When {@link #content()} is called, the original bytes are obtained, passed to
 * the binding function, and the contents of the resulting {@code Bytes} are
 * returned.
 * <p>
 * This allows chaining operations on binary data where each step depends on the
 * actual content of the previous step, similar to {@code flatMap} on streams.
 * It is typically used to build complex byte transformations in a declarative
 * manner, such as parsing a session cookie and then using the extracted value
 * to construct a new request.
 */
public final class BindOfBytes implements Bytes {

    /**
     * The original byte content used as input to the binding function.
     */
    private final Bytes origin;

    /**
     * The function that transforms the original byte array into a new
     * {@link Bytes} instance.
     */
    private final Function<byte[], Bytes> binding;

    /**
     * Creates a new bound byte value from the given original bytes and binding
     * function.
     *
     * @param bs the original byte content
     * @param f  the function that maps the original byte array to a new
     *           {@code Bytes} instance
     */
    public BindOfBytes(final Bytes bs, final Function<byte[], Bytes> f) {
        this.origin = bs;
        this.binding = f;
    }

    /**
     * Returns the content of the {@code Bytes} produced by applying the binding
     * function to the original byte content.
     * <p>
     * The original bytes are obtained via {@link Bytes#content()}, passed to
     * the binding function, and the content of the resulting {@code Bytes} is
     * returned.
     *
     * @return the resulting byte array
     * @throws InvariantViolation if an invariant is violated while retrieving
     *         the original content or evaluating the binding
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return this.binding.apply(
            this.origin.content()
        ).content();
    }
}

package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A {@link Bytes} decorator that memoizes the content of another byte sequence.
 * <p>
 * This class wraps a {@link Bytes} instance and caches the result of its
 * {@link Bytes#content()} method on the first call. Subsequent calls return the
 * cached value without re-evaluating the underlying sequence, which is useful
 * for expensive operations such as network requests or complex byte
 * transformations that should be performed only once.
 * <p>
 * The cached result is stored in an {@link AtomicReference} and guarded by an
 * {@link AtomicBoolean} flag indicating whether the value has already been
 * computed. The returned byte array is always a defensive copy, so callers
 * cannot mutate the cached state.
 * <p>
 * Note that this implementation uses a simple check-then-act pattern without
 * locking. Under concurrent access, it is possible that the underlying
 * {@code Bytes} is evaluated more than once before the cache is populated;
 * however, once evaluated, all callers will observe a consistent cached value.
 * This trade-off avoids synchronization overhead at the cost of potentially
 * redundant computation in rare race scenarios.
 */
public final class Memo implements Bytes {

    /**
     * The underlying byte sequence whose content is memoized.
     */
    private final Bytes origin;

    /**
     * The cached byte content. Initially empty and replaced on first
     * evaluation.
     */
    private final AtomicReference<byte[]> cache;

    /**
     * A flag indicating whether the underlying content has already been
     * evaluated and cached.
     */
    private final AtomicBoolean evaluated;

    /**
     * Creates a memoizing wrapper around the given byte sequence.
     *
     * @param origin the underlying byte sequence whose content will be cached
     */
    public Memo(final Bytes origin) {
        this.origin = origin;
        this.cache = new AtomicReference<>(new byte[0]);
        this.evaluated = new AtomicBoolean(false);
    }

    /**
     * Returns the content of the underlying byte sequence, computing it at most
     * once and caching the result.
     * <p>
     * On the first invocation, the underlying {@link Bytes#content()} method is
     * called, its result is stored in the cache, and the evaluated flag is set.
     * Subsequent invocations return the cached value without re-evaluating the
     * origin. In all cases, a defensive copy of the cached bytes is returned.
     *
     * @return a copy of the (possibly cached) byte content
     * @throws InvariantViolation if an invariant is violated while evaluating
     *         the underlying byte sequence for the first time
     */
    @Override
    public byte[] content() throws InvariantViolation {
        byte[] result = this.cache.get();
        if (!evaluated.get()) {
            result = this.origin.content();
            this.cache.set(result);
            this.evaluated.set(true);
        }
        return result.clone();
    }
}

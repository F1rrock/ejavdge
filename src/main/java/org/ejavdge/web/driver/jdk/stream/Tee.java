package org.ejavdge.web.driver.jdk.stream;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A utility that duplicates a stream into two independently consumable streams,
 * buffering elements as needed.
 * <p>
 * This class behaves like the Unix {@code tee} command: it takes a single
 * underlying stream and exposes two streams ({@link #left()} and
 * {@link #right()}) that each produce the same sequence of elements, in the
 * same order. Elements are read from the underlying source on demand, and
 * elements consumed by one branch that have not yet been consumed by the other
 * are buffered in queues so that they can be replayed later.
 * <p>
 * The implementation maintains two queues, one for each branch. When an element
 * is requested from a branch, the branch first checks its own queue for any
 * buffered elements. If the queue is empty, the next element is pulled from the
 * underlying iterator, added to the other branch's queue, and returned. This
 * ensures that each element is read from the source exactly once, and that both
 * branches observe the same sequence regardless of the order in which they are
 * consumed.
 * <p>
 * This is used by the socket-based web driver to parse an HTTP response in a
 * single pass while simultaneously extracting the header block and the body:
 * the header parsing logic consumes one branch of the tee up to the blank line,
 * and the body stream reads from the other branch, which includes the same
 * bytes plus everything after the header block.
 * <p>
 * Note that a {@code Tee} instance is stateful and not thread-safe. Both
 * branches should be consumed from a single thread, and the class does not
 * support concurrent access.
 *
 * @param <T> the type of elements in the stream
 */
public final class Tee<T> {

    /**
     * The underlying iterator that provides the original sequence of elements.
     */
    private final Iterator<T> it;

    /**
     * The queue buffering elements for the left branch.
     */
    private final Queue<T> left;

    /**
     * The queue buffering elements for the right branch.
     */
    private final Queue<T> right;

    /**
     * Creates a tee over the given stream.
     *
     * @param s the stream to duplicate
     */
    public Tee(final Stream<T> s) {
        this(s.iterator());
    }

    /**
     * Creates a tee over the given iterator.
     *
     * @param it the iterator providing the original sequence of elements
     */
    public Tee(final Iterator<T> it) {
        this.it = it;
        this.left = new ArrayDeque<>();
        this.right = new ArrayDeque<>();
    }

    /**
     * Returns the left branch of this tee.
     * <p>
     * The returned stream produces the same sequence of elements as the
     * underlying source, in the same order. Elements that have already been
     * consumed by the right branch but not yet by this branch are served from
     * the left queue; otherwise, new elements are pulled from the source and
     * buffered for the right branch.
     *
     * @return the left branch as a stream
     */
    public Stream<T> left() {
        return this.stream(this.left, this.right);
    }

    /**
     * Returns the right branch of this tee.
     * <p>
     * The returned stream produces the same sequence of elements as the
     * underlying source, in the same order. Elements that have already been
     * consumed by the left branch but not yet by this branch are served from
     * the right queue; otherwise, new elements are pulled from the source and
     * buffered for the left branch.
     *
     * @return the right branch as a stream
     */
    public Stream<T> right() {
        return this.stream(this.right, this.left);
    }

    /**
     * Creates a stream backed by the given queue, using the other queue to
     * buffer elements consumed from the underlying iterator.
     * <p>
     * When an element is requested, the stream first checks its own queue. If
     * an element is available there, it is removed and passed to the consumer.
     * Otherwise, the stream advances the underlying iterator, adds the
     * retrieved element to the other branch's queue, and passes it to the
     * consumer.
     *
     * @param own   the queue buffering elements for this branch
     * @param other the queue buffering elements for the opposite branch
     * @return a stream backed by this branch's queue and the underlying
     *         iterator
     */
    private Stream<T> stream(final Queue<T> own, final Queue<T> other) {
        return StreamSupport.stream(
            new Spliterators.AbstractSpliterator<>(Long.MAX_VALUE, Spliterator.ORDERED) {
                @Override
                public boolean tryAdvance(final Consumer<? super T> action) {
                    if (!own.isEmpty()) {
                        action.accept(own.remove());
                        return true;
                    }
                    if (!it.hasNext()) {
                        return false;
                    }
                    final var value = it.next();
                    other.add(value);
                    action.accept(value);
                    return true;
                }
            },
            false
        );
    }
}

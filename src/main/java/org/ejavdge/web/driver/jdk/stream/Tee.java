package org.ejavdge.web.driver.jdk.stream;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public final class Tee<T> {
    private final Iterator<T> it;
    private final Queue<T> left;
    private final Queue<T> right;

    public Tee(final Stream<T> s) {
        this(s.iterator());
    }

    public Tee(final Iterator<T> it) {
        this.it = it;
        this.left = new ArrayDeque<>();
        this.right = new ArrayDeque<>();
    }

    public Stream<T> left() {
        return this.stream(this.left, this.right);
    }

    public Stream<T> right() {
        return this.stream(this.right, this.left);
    }

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

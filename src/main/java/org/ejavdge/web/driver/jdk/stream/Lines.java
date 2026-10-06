package org.ejavdge.web.driver.jdk.stream;

import java.util.stream.Stream;

/**
 * A utility that splits a {@link ByteStream} into individual lines.
 * <p>
 * Each line is represented as an {@code int[]} of byte values, without the
 * line separator itself. The splitting is performed lazily and in a streaming
 * fashion, so the entire input does not need to be buffered in memory. This
 * makes it suitable for processing HTTP header blocks and chunked bodies
 * directly from a socket.
 * <p>
 * The implementation is built on {@link Stream#iterate(Object, java.util.function.Predicate, java.util.function.UnaryOperator)}
 * and {@link Tee}. At each step, the current stream is duplicated by a
 * {@code Tee}: the left branch is consumed up to (but not including) the next
 * separator to form a single line, while the right branch is advanced past the
 * separator and used as the input for the next iteration. The iteration
 * continues indefinitely, yielding one line per step; callers are expected to
 * limit the number of lines they consume, for example with
 * {@link Stream#takeWhile(java.util.function.Predicate)}.
 * <p>
 * By default, the separator is a newline character ({@code '\n'}), but it can
 * be customized via the second constructor.
 */
public final class Lines {

    /**
     * The source byte stream to be split into lines.
     */
    private final ByteStream src;

    /**
     * The separator character that delimits lines in the source stream.
     */
    private final char sep;

    /**
     * Creates a line splitter for the given byte stream, using a newline as the
     * default separator.
     *
     * @param s the byte stream to split into lines
     */
    public Lines(final ByteStream s) {
        this(s, '\n');
    }

    /**
     * Creates a line splitter for the given byte stream, using the specified
     * separator character.
     *
     * @param s   the byte stream to split into lines
     * @param sep the character that delimits lines in the source stream
     */
    public Lines(final ByteStream s, char sep) {
        this.src = s;
        this.sep = sep;
    }

    /**
     * Returns a stream of lines extracted from the source byte stream.
     * <p>
     * Each element of the returned stream is an {@code int[]} containing the
     * byte values of a single line, excluding the separator character. The
     * stream is infinite: it continues producing lines until the underlying
     * source is exhausted, at which point further elements are empty. Callers
     * should limit consumption appropriately, for example by stopping when an
     * empty line is encountered.
     * <p>
     * The implementation uses {@link Stream#iterate(Object, java.util.function.Predicate, java.util.function.UnaryOperator)}
     * with a {@link Tee} at each step to peel off one line at a time. The left
     * branch of the tee is consumed up to the next separator and converted into
     * a line; the right branch, which starts immediately after the separator,
     * becomes the input for the next iteration.
     *
     * @return a stream of lines, each represented as an array of byte values
     */
    public Stream<int[]> content() {
        return Stream.iterate(
            new Tee<>(this.src.content().boxed()),
            ignored -> true,
            tee -> new Tee<>(
                tee.right().dropWhile(el -> el != this.sep).skip(1)
            )
        ).map(tee -> tee.left()
            .takeWhile(el -> el != this.sep)
            .mapToInt(Integer::intValue)
            .toArray()
        );
    }
}

package org.ejavdge.web.driver.jdk.stream;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A {@link ByteStream} that concatenates a stream of lines into a single
 * continuous stream of bytes, inserting a separator between each line.
 * <p>
 * This class is the inverse of {@link Lines}: while {@code Lines} splits a
 * byte stream into individual lines, {@code ConcatOfLines} joins a stream of
 * lines back into a single stream. Each line is represented as an {@code int[]}
 * of byte values, and a separator character is appended after every line,
 * including the last one.
 * <p>
 * This is useful when a sequence of lines needs to be reconstructed into a
 * contiguous byte stream, such as when re-emitting an HTTP header block or
 * reassembling a decoded response body. The default separator is a newline
 * character ({@code '\n'}), but it can be customized via the second
 * constructor.
 */
public final class ConcatOfLines implements ByteStream {

    /**
     * The separator character appended after each line.
     */
    private final char sep;

    /**
     * The stream of lines to concatenate, each represented as an array of byte
     * values.
     */
    private final Stream<int[]> src;

    /**
     * Creates a concatenation of the given lines using a newline as the default
     * separator.
     *
     * @param src the stream of lines to concatenate
     */
    public ConcatOfLines(final Stream<int[]> src) {
        this('\n', src);
    }

    /**
     * Creates a concatenation of the given lines using the specified separator.
     *
     * @param sep the character inserted after each line
     * @param src the stream of lines to concatenate
     */
    public ConcatOfLines(final char sep, final Stream<int[]> src) {
        this.sep = sep;
        this.src = src;
    }

    /**
     * Returns the concatenated stream of bytes.
     * <p>
     * Each line in the input stream is flattened into its individual byte
     * values, followed by the separator character. The resulting stream
     * contains the bytes of all lines in order, with the separator inserted
     * after each one.
     *
     * @return the concatenated stream of byte values
     */
    @Override
    public IntStream content() {
        return this.src.flatMapToInt(
            s -> IntStream.concat(
                IntStream.of(s),
                IntStream.of(this.sep)
            )
        );
    }
}

package org.ejavdge.web.driver.jdk.socket.body;

import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumOfHex;
import org.ejavdge.scalar.text.Trimmed;
import org.ejavdge.scalar.text.Utf8Text;
import org.ejavdge.web.driver.jdk.stream.*;

import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

/**
 * A body decoding policy that handles chunked transfer encoding.
 * <p>
 * When an HTTP response uses {@code Transfer-Encoding: chunked}, the body is
 * transmitted as a sequence of chunks. Each chunk consists of:
 * <ol>
 *   <li>a line containing the chunk size in hexadecimal, terminated by a
 *       separator character (typically {@code CRLF}, but this implementation
 *       defaults to {@code '\n'});</li>
 *   <li>exactly that many bytes of chunk data;</li>
 *   <li>a trailing separator sequence (e.g., {@code CRLF}).</li>
 * </ol>
 * The sequence is terminated by a zero-length chunk. This class implements a
 * {@link UnaryOperator UnaryOperator&lt;IntStream&gt;} that decodes such a
 * stream into a contiguous stream of the concatenated chunk data.
 * <p>
 * The decoding algorithm proceeds recursively:
 * <ol>
 *   <li>Read the first line to obtain the chunk size in hexadecimal.</li>
 *   <li>If the size is zero, the end of the body has been reached; return an
 *       empty stream.</li>
 *   <li>Otherwise, read exactly {@code size} bytes as the payload of this
 *       chunk.</li>
 *   <li>Skip the trailing separator sequence (two bytes by default).</li>
 *   <li>Recursively decode the remainder of the stream and concatenate it
 *       after the current payload.</li>
 * </ol>
 * <p>
 * The implementation uses a {@link Tee} to split the input stream into lines
 * and their underlying byte sequences, allowing the first line (the size) to
 * be processed separately from the rest of the stream.
 */
public final class WithChunks implements UnaryOperator<IntStream> {

    /**
     * The separator character used to delimit lines in the chunked stream.
     */
    private final char sep;

    /**
     * Creates a new chunked decoding policy using the default line separator
     * {@code '\n'}.
     */
    public WithChunks() {
        this('\n');
    }

    /**
     * Creates a new chunked decoding policy using the given line separator.
     *
     * @param sep the character used to separate lines in the chunked stream
     */
    public WithChunks(final char sep) {
        this.sep = sep;
    }

    /**
     * Applies the chunked decoding to the given stream of bytes.
     * <p>
     * The stream is expected to contain one or more chunks in the format
     * described in the class documentation. The method reads the first line to
     * determine the size of the next chunk. If the size is zero, it returns an
     * empty stream. Otherwise, it extracts exactly that many bytes as the
     * payload, skips the trailing separator sequence (two bytes), and then
     * recursively decodes the remainder of the stream. The payloads are
     * concatenated in order.
     *
     * @param s the raw chunked body stream as a sequence of byte values
     * @return a stream containing the concatenated payloads of all chunks
     */
    @Override
    public IntStream apply(final IntStream s) {
        final var lines = new Tee<>(
            new Lines(new ByteStream.Of(s), this.sep).content()
        );
        final int size = lines.left()
            .findFirst()
            .map(BytesOfLine::new)
            .map(Utf8Text::new)
            .map(Trimmed::new)
            .map(NumOfHex::new)
            .map(Num::value)
            .orElse(0);
        if (size == 0) {
            return IntStream.empty();
        }
        final var payload = new Tee<>(
            new ConcatOfLines(
                this.sep,
                lines.right().skip(1)
            ).content().boxed()
        );
        return IntStream.concat(
            payload.left()
                .limit(size)
                .mapToInt(Integer::intValue),
            this.apply(
                payload.right()
                    .skip(size)
                    .skip(2)
                    .mapToInt(Integer::intValue)
            )
        );
    }
}

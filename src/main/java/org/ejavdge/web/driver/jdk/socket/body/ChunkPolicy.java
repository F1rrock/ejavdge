package org.ejavdge.web.driver.jdk.socket.body;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Match;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.resource.TransferEncoding;

import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

/**
 * A body decoding policy that handles chunked transfer encoding.
 * <p>
 * When an HTTP response uses {@code Transfer-Encoding: chunked}, the body is
 * transmitted as a sequence of chunks rather than as a single contiguous
 * block. Each chunk is prefixed by its length in hexadecimal, followed by the
 * chunk data and a CRLF terminator, and the sequence is terminated by a
 * zero-length chunk. This class implements a
 * {@link UnaryOperator UnaryOperator&lt;IntStream&gt;} that detects whether the
 * response uses chunked transfer encoding and, if so, applies the
 * {@link WithChunks} decoder to the body stream.
 * <p>
 * The detection is performed by extracting the {@code Transfer-Encoding}
 * header from the raw response bytes (which are expected to contain the
 * headers) and matching it against the literal string {@code "chunked"}. If
 * the header value contains {@code "chunked"}, the chunked decoding is applied.
 * Otherwise, the request is delegated to a fallback operator supplied at
 * construction time — typically a policy that handles {@code Content-Length}
 * or reports that the encoding is unsupported.
 */
public final class ChunkPolicy implements UnaryOperator<IntStream> {

    /**
     * A matcher used to detect the presence of {@code "chunked"} in the
     * {@code Transfer-Encoding} header.
     */
    private final Text match;

    /**
     * The fallback policy applied when the response does not use chunked
     * transfer encoding.
     */
    private final UnaryOperator<IntStream> next;

    /**
     * Creates a chunked transfer decoding policy from the given raw response
     * bytes and fallback operator.
     * <p>
     * The {@code Transfer-Encoding} header is extracted from the raw response
     * bytes via {@link TransferEncoding}, and a matcher is set up to detect the
     * presence of the literal string {@code "chunked"} in that header.
     *
     * @param bs  the raw response bytes containing the headers used to detect
     *            chunked transfer encoding
     * @param nxt the fallback operator to apply if chunked encoding is not
     *            detected
     */
    public ChunkPolicy(final byte[] bs, final UnaryOperator<IntStream> nxt) {
        this.match = new Match(
            new Text.Of("chunked"),
            new TransferEncoding(
                new Bytes.Of(bs)
            )
        );
        this.next = nxt;
    }

    /**
     * Applies this policy to the given response body stream.
     * <p>
     * The method first attempts to match the literal string {@code "chunked"}
     * within the {@code Transfer-Encoding} header. If the match succeeds, the
     * body is decoded using {@link WithChunks}. If the match fails — that is,
     * the response does not use chunked transfer encoding — the fallback
     * operator is applied to the stream instead.
     *
     * @param s the raw response body stream
     * @return the decoded body stream, either via chunked decoding or via the
     *         fallback operator
     */
    @Override
    public IntStream apply(final IntStream s) {
        try {
            this.match.content();
        } catch (InvariantViolation e) {
            return this.next.apply(s);
        }
        return new WithChunks().apply(s);
    }
}

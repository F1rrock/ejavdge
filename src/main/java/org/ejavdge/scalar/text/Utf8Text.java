package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;

/**
 * An adapter that decodes a {@link Bytes} sequence into a {@link Text} using
 * UTF-8.
 * <p>
 * This class implements {@link Text} and wraps a {@link Bytes} instance. When
 * {@link #content()} is called, the underlying bytes are materialized and
 * decoded as UTF-8 text. The decoding is strict: malformed input and unmappable
 * characters are reported as errors rather than silently replaced. This ensures
 * that invalid byte sequences do not go unnoticed.
 * <p>
 * If the bytes cannot be decoded as valid UTF-8, an
 * {@link InvariantViolation} is thrown with a message indicating that the
 * content is not valid UTF-8, and the original
 * {@link CharacterCodingException} is attached as the cause.
 */
public final class Utf8Text implements Text {

    /**
     * The underlying byte sequence to be decoded as UTF-8.
     */
    private final Bytes src;

    /**
     * Creates a UTF-8 text view over the given byte sequence.
     *
     * @param src the bytes to decode as UTF-8
     */
    public Utf8Text(final Bytes src) {
        this.src = src;
    }

    /**
     * Returns the UTF-8 decoded content of the underlying bytes.
     * <p>
     * The bytes are materialized via {@link Bytes#content()} and decoded using
     * a UTF-8 decoder configured to report malformed input and unmappable
     * characters. If decoding succeeds, the resulting string is returned. If
     * the bytes are not valid UTF-8, an {@link InvariantViolation} is thrown
     * with the message {@code "Bytes do not contain valid UTF-8\n"} and the
     * underlying {@link CharacterCodingException} as the cause.
     *
     * @return the decoded text content
     * @throws InvariantViolation if the bytes are not valid UTF-8 or if an
     *         invariant is violated while retrieving the byte content
     */
    @Override
    public String content() throws InvariantViolation {
        try {
            return StandardCharsets.UTF_8
                .newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)
                .decode(ByteBuffer.wrap(this.src.content()))
                .toString();
        } catch (final CharacterCodingException e) {
            throw new InvariantViolation(
                "Bytes do not contain valid UTF-8\n",
                e
            );
        }
    }
}

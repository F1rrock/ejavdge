package org.ejavdge.scalar.text;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;

public final class Utf8Text implements Text {
    private final Bytes src;

    public Utf8Text(final Bytes src) {
        this.src = src;
    }

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
                "Bytes do not contain valid UTF-8"
            );
        }
    }
}
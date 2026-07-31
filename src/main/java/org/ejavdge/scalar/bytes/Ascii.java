package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

import java.nio.charset.StandardCharsets;

public final class Ascii implements Bytes {
    private final Text src;

    public Ascii(final Text t) {
        this.src = t;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        final var s = this.src.content();
        if (!s.chars().allMatch(el -> el <= 127)) {
            throw new InvariantViolation(
                "Text does not consist only of ASCII characters"
            );
        }
        return this.src
            .content()
            .getBytes(StandardCharsets.US_ASCII);
    }
}

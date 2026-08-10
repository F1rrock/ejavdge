package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Lowers;
import org.ejavdge.scalar.text.Match;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.scalar.text.Utf8Text;

public final class TransferEncoding implements Text {
    private final Text origin;

    public TransferEncoding(final Bytes src) {
        this.origin = new TextAbout(
            "transfer-encoding",
            new Match(
                new Lowers(
                    new Utf8Text(src)
                ),
                new Text.Of(
                    "(?<=transfer-encoding:\\s*)\\S+"
                )
            )
        );
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}

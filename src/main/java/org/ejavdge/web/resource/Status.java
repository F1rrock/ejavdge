package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.num.NumOfText;
import org.ejavdge.scalar.num.Positive;
import org.ejavdge.scalar.text.Match;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Utf8Text;

public final class Status implements Num {
    private final Num origin;

    public Status(final Bytes src) {
        this.origin = new NumAbout(
            "status",
            new Positive(
                new NumOfText(
                    new Match(
                        new Utf8Text(src),
                        new Text.Of(
                            "(?<=HTTP/\\d\\.\\d )\\d{3}"
                        )
                    )
                )
            )
        );
    }

    @Override
    public int value() throws InvariantViolation {
        return this.origin.value();
    }
}

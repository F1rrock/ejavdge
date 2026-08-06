package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumOfText;
import org.ejavdge.scalar.text.Lowers;
import org.ejavdge.scalar.text.Match;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Utf8Text;

public final class ContentLength implements Num {
    private final Num origin;

    public ContentLength(final Bytes src) {
        this.origin = new NumOfText(
            new Match(
                new Lowers(
                    new Utf8Text(src)
                ),
                new Text.Of(
                    "(?<=content-length:\\s*)\\d+"
                )
            )
        );
    }

    @Override
    public int value() throws InvariantViolation {
        return this.origin.value();
    }
}

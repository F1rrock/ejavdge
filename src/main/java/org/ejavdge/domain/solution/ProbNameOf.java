package org.ejavdge.domain.solution;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.file.ContentOf;
import org.ejavdge.scalar.text.*;

public final class ProbNameOf implements Text {
    private final Text origin;

    public ProbNameOf(final ByteFile f) {
        this(
            new TextAbout(
                "problem's marker",
                new NonEmpty(
                    new Match(
                        new Utf8Text(
                            new ContentOf(f)
                        ),
                        new Text.Of(
                            "(?m)(?<=^//\\s{0,20}problem:\\s{0,20})[A-Za-z][A-Za-z0-9]*"
                        )
                    )
                )
            )
        );
    }

    public ProbNameOf(final Text t) {
        this.origin = t;
    }

    @Override
    public String content() throws InvariantViolation {
        return this.origin.content();
    }
}

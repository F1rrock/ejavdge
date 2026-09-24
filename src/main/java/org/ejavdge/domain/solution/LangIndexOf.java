package org.ejavdge.domain.solution;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.file.ContentOf;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.num.NumOfText;
import org.ejavdge.scalar.num.Positive;
import org.ejavdge.scalar.text.*;

public final class LangIndexOf implements Num {
    private final Num origin;

    public LangIndexOf(final ByteFile f) {
        this(
            new NumAbout(
                "language marker",
                new Positive(
                    new NumOfText(
                        new NonEmpty(
                            new Match(
                                new Utf8Text(
                                    new ContentOf(f)
                                ),
                                new Text.Of(
                                    "(?m)(?<=^//\\s{0,20}language:\\s{0,20})\\d+"
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    public LangIndexOf(final Num n) {
        this.origin = n;
    }

    @Override
    public int value() throws InvariantViolation {
        return this.origin.value();
    }
}

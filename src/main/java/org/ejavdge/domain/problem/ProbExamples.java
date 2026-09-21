package org.ejavdge.domain.problem;

import org.ejavdge.domain.Fixture;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.*;
import org.ejavdge.scalar.text.*;

import java.util.List;

public final class ProbExamples implements Items<Fixture> {
    private final Items<Fixture> origin;

    public ProbExamples(final ProbBrief b) {
        this(
            new Map<>(
                ls -> new Fixture.Of(
                    new TextAbout(
                        "input data of problem example",
                        new Trimmed(
                            new Concat(
                                new Text.Of("\n"),
                                new OnlyUntil<>(
                                    l -> l.equals(new ContentBased("Output")),
                                    ls
                                )
                            )
                        )
                    ),
                    new TextAbout(
                        "output data of problem example",
                        new Trimmed(
                            new Concat(
                                new Text.Of("\n"),
                                new OnlyAfter<>(
                                    l -> l.equals(new ContentBased("Output")),
                                    ls
                                )
                            )
                        )
                    )
                ),
                new Map<>(
                    ls -> new BindOfItems<>(ls, Items.Of::new),
                    new WithoutFirst<>(
                        new Split<>(
                            l -> l.equals(new ContentBased("Input")),
                            new OnlyAfter<>(
                                l -> l.equals(new ContentBased("Examples")),
                                new Map<>(
                                    ContentBased::new,
                                    new Lines(b)
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    public ProbExamples(final Items<Fixture> xs) {
        this.origin = xs;
    }

    @Override
    public List<Fixture> contents() throws InvariantViolation {
        return this.origin.contents();
    }
}

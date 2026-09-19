package org.ejavdge.domain.problem;

import org.ejavdge.contest.MainPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.*;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.num.NumOfText;
import org.ejavdge.scalar.text.Match;
import org.ejavdge.scalar.text.Text;

public final class ProbByName implements Num {
    private final Num origin;

    public ProbByName(final XmlEngine e, final MainPage p, final Text t) {
        this(
            new NumAbout(
                "problem's id",
                new NumOfText(
                    new Match(
                        new XmlSelection(
                            e, p,
                            new LinksOnly(
                                new WithText(
                                    t,
                                    new WithClass(
                                        "tab",
                                        new NestedTag(
                                            "a",
                                            new WithClass(
                                                "nTopNavList",
                                                new NestedTag(
                                                    "ul",
                                                    new WithId(
                                                        "probNavTopList",
                                                        new OnlyTag("tr")
                                                    )
                                                )
                                            )
                                        )
                                    )
                                )
                            )
                        ),
                        new Text.Of("(?<=prob_id=\\s*)\\d+")
                    )
                )
            )
        );
    }

    public ProbByName(final Num n) {
        this.origin = n;
    }

    @Override
    public int value() throws InvariantViolation {
        return this.origin.value();
    }
}

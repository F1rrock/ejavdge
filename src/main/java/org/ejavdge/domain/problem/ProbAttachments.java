package org.ejavdge.domain.problem;

import org.ejavdge.contest.ProblemPage;
import org.ejavdge.dom.XmlSelection;
import org.ejavdge.dom.engine.XmlEngine;
import org.ejavdge.dom.path.AllNodes;
import org.ejavdge.dom.path.LinksOnly;
import org.ejavdge.dom.path.WithName;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.*;
import org.ejavdge.scalar.text.ContentBased;
import org.ejavdge.scalar.text.Empty;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Trimmed;

import java.util.List;

public final class ProbAttachments implements Items<Text> {
    private final Items<Text> origin;

    public ProbAttachments(final XmlEngine e, final ProblemPage p) {
        this(
            new ItemsAbout<>(
                "problem's file references",
                new OnlyWhere<>(
                    l -> !new ContentBased(l).equals(new ContentBased(new Empty())),
                    new Map<>(
                        Trimmed::new,
                        new Lines(
                            new XmlSelection(
                                e, p,
                                new LinksOnly(
                                    new WithName(
                                        "localtest",
                                        new AllNodes()
                                    )
                                )
                            )
                        )
                    )
                )
            )
        );
    }

    public ProbAttachments(final Items<Text> ts) {
        this.origin = ts;
    }

    @Override
    public List<Text> contents() throws InvariantViolation {
        return this.origin.contents();
    }
}

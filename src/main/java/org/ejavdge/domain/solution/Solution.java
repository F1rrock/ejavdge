package org.ejavdge.domain.solution;

import org.ejavdge.contest.ContestForm;
import org.ejavdge.effect.Envelope;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.items.Items;
import org.ejavdge.items.Joint;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.ContextOfSolution;
import org.ejavdge.web.context.WithEntry;
import org.ejavdge.web.spec.body.multipart.FilePart;
import org.ejavdge.web.spec.body.multipart.TextParts;

public final class Solution implements Envelope {
    private final Envelope origin;

    public Solution(final ContestForm cf, final ContextOfSolution cs, final ByteFile f) {
        this(
            new ContestForm(
                cf,
                new Joint<>(
                    new TextParts.ImprintOf(
                        new WithEntry(
                            new Text.Of("action_40"),
                            new Text.Of("Send!"),
                            cs
                        )
                    ),
                    new Items.Of<>(new FilePart(f))
                )
            )
        );
    }

    public Solution(final Envelope e) {
        this.origin = e;
    }

    @Override
    public void send() throws InvariantViolation {
        this.origin.send();
    }
}

package org.ejavdge.domain.solution;

import org.ejavdge.domain.Fixture;
import org.ejavdge.domain.Verdict;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.Program;
import org.ejavdge.items.Items;
import org.ejavdge.scalar.text.Text;

public final class VerdictBySamples implements Verdict {
    private final Program program;
    private final Items<Fixture> fixtures;

    public VerdictBySamples(final Program p, final Items<Fixture> fs) {
        this.program = p;
        this.fixtures = fs;
    }

    @Override
    public boolean ok() throws InvariantViolation {
        return this.fixtures.contents()
            .stream()
            .allMatch(
                f -> this.program.outcomeOf(
                    new Text.Of(f.input())
                ).trim().equals(f.expected().trim())
            );
    }
}

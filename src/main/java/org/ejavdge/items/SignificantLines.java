package org.ejavdge.items;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.ContentBased;
import org.ejavdge.scalar.text.Empty;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Trimmed;

import java.util.List;

public final class SignificantLines implements Items<Text> {
    private final Items<Text> origin;

    public SignificantLines(final Text t) {
        this.origin = new OnlyWhere<>(
            l -> !l.equals(new ContentBased(new Empty())),
            new Map<>(
                ContentBased::new,
                new Map<>(
                    Trimmed::new,
                    new Lines(t)
                )
            )
        );
    }

    @Override
    public List<Text> contents() throws InvariantViolation {
        return this.origin.contents();
    }
}
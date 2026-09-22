package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;

public final class LastSegmentOf implements Text {
    private final PathOf path;

    public LastSegmentOf(final PathOf p) {
        this.path = p;
    }

    @Override
    public String content() throws InvariantViolation {
        return new TextAbout(
            "last segment of path",
            () -> {
                final var p = this.path.content();
                final var slash = p.lastIndexOf('/');
                if (slash < 0) {
                    throw new InvariantViolation(
                        "There is no segments in path: " + p
                    );
                }
                return p.substring(slash + 1);
            }
        ).content();
    }
}

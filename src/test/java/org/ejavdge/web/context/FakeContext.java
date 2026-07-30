package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.media.Media;

public final class FakeContext implements Context {
    @Override
    public <T> T imprint(final Media<T> m) throws InvariantViolation {
        return m
            .with(new Text.Of("name 1"), new Text.Of("value 1"))
            .with(new Text.Of("name 2"), new Text.Of("value 2"))
            .content();
    }
}

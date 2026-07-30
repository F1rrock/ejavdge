package org.ejavdge.web.media;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;

public final class FakeMedia implements Media<String> {
    private final String src;

    public FakeMedia() {
        this("");
    }

    public FakeMedia(final String src) {
        this.src = src;
    }

    @Override
    public FakeMedia with(Text name, Text value) {
        return new FakeMedia(
            this.src
                + name.content()
                + ":"
                + value.content()
                + ":"
        );
    }

    @Override
    public String content() throws InvariantViolation {
        return this.src;
    }
}
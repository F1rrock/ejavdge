package org.ejavdge.scalar.text;

public final class Empty implements Text {
    private final Text origin;

    public Empty() {
        this.origin = new Text.Of("");
    }

    @Override
    public String content() {
        return this.origin.content();
    }
}

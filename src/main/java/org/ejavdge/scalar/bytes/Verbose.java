package org.ejavdge.scalar.bytes;

import java.io.PrintStream;

public final class Verbose implements Bytes {

    private final Bytes origin;
    private final String message;

    public Verbose(final Bytes origin, final String message) {
        this.origin = origin;
        this.message = message;
    }

    @Override
    public byte[] content() {
        final PrintStream out = System.out;
        out.println(this.message);
        return this.origin.content();
    }
}
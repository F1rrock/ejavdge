package org.ejavdge.scalar.bytes;

public final class Verbose implements Bytes {

    private final Bytes origin;
    private final String message;

    public Verbose(final Bytes origin, final String message) {
        this.origin = origin;
        this.message = message;
    }

    @Override
    public byte[] content() {
        System.out.println(this.message);
        return this.origin.content();
    }
}
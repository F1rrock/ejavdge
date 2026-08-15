package org.ejavdge.scalar.bytes;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;

import java.io.ByteArrayOutputStream;

public final class Concat implements Bytes {
    private final Items<? extends Bytes> bss;

    public Concat(final Bytes ...b) {
        this(new Items.Of<>(b));
    }

    public Concat(final Items<? extends Bytes> b) {
        this.bss = b;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.bss.contents()
            .stream()
            .map(Bytes::content)
            .collect(
                ByteArrayOutputStream::new,
                ByteArrayOutputStream::writeBytes,
                (l, r) -> l.writeBytes(r.toByteArray())
            )
            .toByteArray();
    }
}

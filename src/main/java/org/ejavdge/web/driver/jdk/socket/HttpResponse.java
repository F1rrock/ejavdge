package org.ejavdge.web.driver.jdk.socket;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Memo;
import org.ejavdge.web.driver.jdk.stream.*;

import java.util.Arrays;
import java.util.stream.IntStream;

public final class HttpResponse {
    private final IntStream stream;
    private final Bytes headers;

    public HttpResponse(final ByteStream bs) {
        this(new Tee<>(bs.content().boxed()), '\r', '\n');
    }

    public HttpResponse(final Tee<Integer> tee, final char cr, final char lf) {
        this.stream = tee.left().mapToInt(Integer::intValue);
        this.headers = new Memo(
            new BytesOfStream(
                new ConcatOfLines(
                    lf,
                    new Lines(
                        new ByteStream.Of(
                            tee.right().mapToInt(Integer::intValue)
                        ),
                        lf
                    ).content().takeWhile(
                        ln -> !Arrays.equals(ln, new int[] {cr})
                    )
                ).content()
            )
        );
    }

    public byte[] headers() throws InvariantViolation {
        return this.headers.content();
    }

    public IntStream body() throws InvariantViolation {
        return this.stream.skip(this.headers.content().length + 2L);
    }
}

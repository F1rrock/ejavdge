package org.ejavdge.web.driver.jdk.socket;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.web.driver.jdk.stream.ByteStream;
import org.ejavdge.web.driver.jdk.stream.Lines;
import org.ejavdge.web.driver.jdk.stream.Tee;

import java.util.Arrays;
import java.util.stream.IntStream;

public final class HttpResponse {
    private final Tee<int[]> tee;
    private final int[] cr;

    public HttpResponse(final ByteStream bs) {
        this.tee = new Tee<>(
            new Lines(bs, '\n').content()
        );
        this.cr = new int[] {'\r'};
    }

    public IntStream headers() throws InvariantViolation {
        return this.tee.left()
            .takeWhile(ln -> !Arrays.equals(ln, this.cr))
            .flatMapToInt(
                ln -> IntStream.concat(
                    IntStream.of(ln),
                    IntStream.of('\n')
                )
            );
    }

    public IntStream body() throws InvariantViolation {
        return this.tee.right()
            .dropWhile(ln -> !Arrays.equals(ln, this.cr))
            .skip(1)
            .flatMapToInt(
                ln -> IntStream.concat(
                    IntStream.of(ln),
                    IntStream.of('\n')
                )
            );
    }
}

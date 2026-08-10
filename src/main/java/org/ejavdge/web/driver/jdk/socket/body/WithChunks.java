package org.ejavdge.web.driver.jdk.socket.body;

import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumOfHex;
import org.ejavdge.scalar.text.Trimmed;
import org.ejavdge.scalar.text.Utf8Text;
import org.ejavdge.web.driver.jdk.stream.*;

import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public final class WithChunks implements UnaryOperator<IntStream> {
    private final char sep;

    public WithChunks() {
        this('\n');
    }

    public WithChunks(final char sep) {
        this.sep = sep;
    }

    @Override
    public IntStream apply(final IntStream s) {
        final var lines = new Tee<>(
            new Lines(new ByteStream.Of(s), this.sep).content()
        );
        final int size = lines.left()
            .findFirst()
            .map(BytesOfLine::new)
            .map(Utf8Text::new)
            .map(Trimmed::new)
            .map(NumOfHex::new)
            .map(Num::value)
            .orElse(0);
        if (size == 0) {
            return IntStream.empty();
        }
        final var payload = new Tee<>(
            new ConcatOfLines(
                this.sep,
                lines.right().skip(1)
            ).content().boxed()
        );
        return IntStream.concat(
            payload.left()
                .limit(size)
                .mapToInt(Integer::intValue),
            this.apply(
                payload.right()
                    .skip(size)
                    .skip(2)
                    .mapToInt(Integer::intValue)
            )
        );
    }
}

package org.ejavdge.web.driver.jdk.socket.body;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Match;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.resource.TransferEncoding;

import java.util.function.UnaryOperator;
import java.util.stream.IntStream;

public final class ChunkPolicy implements UnaryOperator<IntStream> {
    private final Text match;
    private final UnaryOperator<IntStream> next;

    public ChunkPolicy(final byte[] bs, final UnaryOperator<IntStream> nxt) {
        this.match = new Match(
            new Text.Of("chunked"),
            new TransferEncoding(
                new Bytes.Of(bs)
            )
        );
        this.next = nxt;
    }

    @Override
    public IntStream apply(final IntStream s) {
        try {
            this.match.content();
        } catch (InvariantViolation e) {
            return this.next.apply(s);
        }
        return new WithChunks().apply(s);
    }
}

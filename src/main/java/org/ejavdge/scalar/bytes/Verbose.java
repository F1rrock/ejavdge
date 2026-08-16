package org.ejavdge.scalar.bytes;

import org.ejavdge.scalar.text.Text;
import org.slf4j.Logger;

public final class Verbose implements Bytes {

    private final Bytes origin;
    private final Text message;
    private final Logger log;

    public Verbose(final Bytes origin, final Text message, final Logger log) {
        this.origin = origin;
        this.message = message;
        this.log = log;
    }

    public Verbose(final Bytes origin, final String message, final Logger log) {
        this.origin = origin;
        this.message = new Text.Of(message);
        this.log = log;
    }

    @Override
    public byte[] content() {
        if (this.log.isDebugEnabled()) {
            this.log.debug(this.message.content());
        }
        return this.origin.content();
    }
}
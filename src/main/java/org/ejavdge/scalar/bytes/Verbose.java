package org.ejavdge.scalar.bytes;

import org.ejavdge.scalar.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Verbose implements Bytes {
    private final Bytes origin;
    private final Text message;
    private final Logger log;

    public Verbose(final Bytes bs, final String s) {
        this(bs, new Text.Of(s));
    }

    public Verbose(final Bytes bs, final Text t) {
        this(bs, t, LoggerFactory.getLogger(Verbose.class));
    }

    public Verbose(final Bytes bs, final String s, final Logger l) {
        this(bs, new Text.Of(s), l);
    }

    public Verbose(final Bytes bs, final Text t, final Logger l) {
        this.origin = bs;
        this.message = t;
        this.log = l;
    }

    @Override
    public byte[] content() {
        if (this.log.isDebugEnabled()) {
            this.log.debug(this.message.content());
        }
        return this.origin.content();
    }
}
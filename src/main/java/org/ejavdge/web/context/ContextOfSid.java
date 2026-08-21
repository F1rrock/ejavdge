package org.ejavdge.web.context;

import org.ejavdge.auth.Session;
import org.ejavdge.domain.tokens.Sid;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.media.Media;

public final class ContextOfSid implements Context {
    private final Context origin;

    public ContextOfSid(final byte[] bs) {
        this(new Bytes.Of(bs));
    }

    public ContextOfSid(final Bytes bs) {
        this(new Session(bs));
    }

    public ContextOfSid(final Session s) {
        this(new Sid(s));
    }

    public ContextOfSid(final Sid s) {
        this(
            new WithEntry(
                new Text.Of("SID"),
                s
            )
        );
    }

    public ContextOfSid(final Context c) {
        this.origin = c;
    }

    @Override
    public <T> T imprint(final Media<T> m) throws InvariantViolation {
        return this.origin.imprint(m);
    }
}

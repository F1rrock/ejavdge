package org.ejavdge.web.context;

import org.ejavdge.auth.Session;
import org.ejavdge.domain.tokens.Ejsid;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.media.Media;

public final class ContextOfEjsid implements Context {
    private final Context origin;

    public ContextOfEjsid(final byte[] bs) {
        this(new Bytes.Of(bs));
    }

    public ContextOfEjsid(final Bytes bs) {
        this(new Session(bs));
    }

    public ContextOfEjsid(final Session s) {
        this(new Ejsid(s));
    }

    public ContextOfEjsid(final Ejsid s) {
        this(
            new WithEntry(
                new Text.Of("EJSID"),
                s
            )
        );
    }

    public ContextOfEjsid(final Context c) {
        this.origin = c;
    }

    @Override
    public <T> T imprint(final Media<T> m) throws InvariantViolation {
        return this.origin.imprint(m);
    }
}

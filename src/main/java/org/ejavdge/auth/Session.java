package org.ejavdge.auth;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.BytesAbout;
import org.ejavdge.scalar.bytes.Memo;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.resource.HasStatus;

public final class Session implements Bytes {
    private final Bytes src;

    public Session(final WebDriver d, final Location l, final Credentials c) {
        this(
            new BytesAbout(
                "ejudge session",
                new Memo(
                    new HasStatus(
                        new Num.Of(302),
                        new LoginReply(d, l, c)
                    )
                )
            )
        );
    }

    public Session(final Bytes bs) {
        this.src = bs;
    }

    @Override
    public byte[] content() throws InvariantViolation {
        return this.src.content();
    }
}

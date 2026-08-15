package org.ejavdge.auth;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.*;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.resource.HasStatus;
import org.ejavdge.scalar.bytes.Verbose;
import org.ejavdge.scalar.bytes.Timeout;

import java.time.Duration;

public final class Session implements Bytes {
    private final Bytes src;

    public Session(final WebDriver driver, final Location location, final Credentials credentials) {
        this(
            new BytesAbout(
                "ejudge session",
                new Memo(
                    new Retry(
                        () -> new Verbose(
                            new Timeout(
                                new HasStatus(
                                    new Num.Of(302),
                                    new Memo(
                                        new LoginReply(
                                            driver,
                                            location,
                                            credentials
                                        )
                                    )
                                ),
                                Duration.ofSeconds(5)
                            ),
                            "Fetching session..."
                        ),
                        5
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

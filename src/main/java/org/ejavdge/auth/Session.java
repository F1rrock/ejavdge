package org.ejavdge.auth;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.*;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.resource.HasStatus;
import org.ejavdge.scalar.bytes.Verbose;
import org.ejavdge.scalar.bytes.WithTimeout;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public final class Session implements Bytes {
    private final Bytes src;

    public Session(final WebDriver driver, final Location location, final Credentials credentials) {
        this(
            new BytesAbout(
                "ejudge session",
                new Memo(
                    new WithRetry(
                        new Verbose(
                            new WithTimeout(
                                new BindOfBytes(
                                    new LoginReply(
                                        driver,
                                        location,
                                        credentials
                                    ),
                                    bytes -> new HasStatus(
                                        new Num.Of(302),
                                        new Bytes.Of(bytes)
                                    )
                                ),
                                Duration.ofSeconds(5)
                            ),
                            new Text.Of("Fetching session..."),
                            LoggerFactory.getLogger(Session.class)
                        ),
                        new Num.Of(5)
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

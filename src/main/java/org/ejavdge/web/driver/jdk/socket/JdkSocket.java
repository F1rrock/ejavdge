package org.ejavdge.web.driver.jdk.socket;

import org.ejavdge.scalar.bytes.*;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.driver.jdk.socket.body.*;
import org.ejavdge.web.spec.Request;
import org.ejavdge.web.spec.Terminator;

public final class JdkSocket implements WebDriver {
    @Override
    public byte[] resourceOf(final Location loc, final Request req) {
        final var response = new HttpResponse(
            new BytesOfReply(new Reply(loc, req))
        );
        final var headers = new BytesAbout(
            "headers",
            new Memo(new HeadersOf(response))
        );
        return new Concat(
            headers,
            new Terminator(),
            new BytesAbout(
                "body",
                new BindOfBytes(
                    headers,
                    bs -> new BodyOf(
                        response,
                        new BodyPolicy(bs)
                    )
                )
            )
        ).content();
    }
}

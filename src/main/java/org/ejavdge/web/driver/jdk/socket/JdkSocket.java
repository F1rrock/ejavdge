package org.ejavdge.web.driver.jdk.socket;

import org.ejavdge.scalar.bytes.Concat;
import org.ejavdge.scalar.bytes.Memo;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.resource.ContentLength;
import org.ejavdge.web.spec.Request;
import org.ejavdge.web.spec.Terminator;

public final class JdkSocket implements WebDriver {
    @Override
    public byte[] resourceOf(final Location loc, final Request req) {
        final var response = new HttpResponse(
            new BytesOfReply(
                new Reply(loc, req)
            )
        );
        final var headers = new Memo(
            new HeadersOf(response)
        );
        return new Concat(
            headers,
            new Terminator(),
            new BodyOf(
                response,
                new ContentLength(headers)
            )
        ).content();
    }
}

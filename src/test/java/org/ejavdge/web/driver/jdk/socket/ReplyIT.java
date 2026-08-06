package org.ejavdge.web.driver.jdk.socket;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.spec.HttpSpec;
import org.ejavdge.web.spec.Request;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;

public final class ReplyIT extends TestCase {
    public void testHttpResponse() {
        try (final var server = new ServerSocket(0)) {
            new Thread(() -> {
                try (final var client = server.accept()) {
                    this.request(client.getInputStream());
                    final OutputStream out = client.getOutputStream();
                    out.write(
                        "HTTP/1.1 200 OK\r\n\r\nHello"
                            .getBytes(StandardCharsets.UTF_8)
                    );
                    out.flush();
                } catch (final IOException err) {
                    fail("Server error: " + err.getMessage());
                }
            }).start();
            final var reply = new Reply(
                new Location(
                    new Text.Of("/"),
                    new Text.Of("localhost"),
                    new Num.Of(server.getLocalPort())
                ),
                new Request(
                    new HttpSpec.Of(
                        "GET / HTTP/1.1\r\nHost: localhost\r\n"
                            .getBytes(StandardCharsets.UTF_8)
                    )
                )
            );
            try (var stream = reply.stream()) {
                final var actual = new String(
                    stream.readAllBytes(),
                    StandardCharsets.UTF_8
                );
                assertTrue(actual.contains("Hello"));
            }
        } catch (final Exception err) {
            fail(err.getMessage());
        }
    }

    public void testConnectionRefused() {
        final var reply = new Reply(
            new Location(
                new Text.Of("/"),
                new Text.Of("localhost"),
                new Num.Of(9999)
            ),
            new Request(
                new HttpSpec.Of(
                    "GET / HTTP/1.1\r\nHost: localhost\r\n"
                        .getBytes(StandardCharsets.UTF_8)
                )
            )
        );
        try {
            reply.stream().close();
        } catch (final InvariantViolation e) {
            return;
        } catch (IOException e) {
            fail(e.getMessage());
        }
        fail("InvariantViolation");
    }

    private void request(final InputStream in) throws IOException {
        final var bytes = new StringBuilder();
        int current;
        while ((current = in.read()) != -1) {
            bytes.append((char) current);
            if (bytes.toString().contains("\r\n\r\n")) {
                break;
            }
        }
    }
}

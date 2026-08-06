package org.ejavdge.web.driver.jdk.socket;

import junit.framework.TestCase;
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

public final class BytesOfReplyIT extends TestCase {
    public void testResponseBytes() {
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
                    throw new RuntimeException(err);
                }
            }).start();
            final var bytes = new BytesOfReply(
                new Reply(
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
                )
            );
            final var actual = bytes.content()
                .boxed()
                .toList();
            final var text = actual.stream()
                .map(i -> Character.toString((char) i.intValue()))
                .collect(java.util.stream.Collectors.joining());
            assertTrue(text.contains("200 OK"));
            assertTrue(text.contains("Hello"));
        } catch (final Exception err) {
            fail(err.getMessage());
        }
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

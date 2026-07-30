package org.ejavdge.web.driver.jdk.socket;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Location;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;

public final class InetIT extends TestCase {
    public void testWorkingSocket() {
        try (final var server = new ServerSocket(0)) {
            final int port = server.getLocalPort();
            new Thread(() -> {
                try (final var client = server.accept()) {
                    final InputStream in = client.getInputStream();
                    final var arr = new ByteArrayOutputStream();
                    final byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        arr.write(buffer, 0, bytesRead);
                        if (arr.toString().contains("\r\n\r\n")) {
                            break;
                        }
                    }
                    final OutputStream out = client.getOutputStream();
                    out.write(
                        "HTTP/1.1 200 OK\r\n\r\nHello from Inet"
                            .getBytes(StandardCharsets.US_ASCII)
                    );
                    out.flush();
                } catch (final IOException e) {
                    fail("Server error: " + e.getMessage());
                }
            }).start();
            final var inet = new Inet(
                new Location(
                    new Text.Of("/test"),
                    new Text.Of("localhost"),
                    new Num.Of(port)
                )
            );
            try (final var socket = inet.socket()) {
                final OutputStream out = socket.getOutputStream();
                out.write(
                    "GET / HTTP/1.1\r\nHost: localhost\r\n\r\n"
                        .getBytes(StandardCharsets.US_ASCII)
                );
                out.flush();
                final var in = socket.getInputStream();
                final var response = new ByteArrayOutputStream();
                final byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    response.write(buffer, 0, bytesRead);
                }
                final var r = response.toString(StandardCharsets.US_ASCII);
                assertTrue(r.contains("200 OK"));
                assertTrue(r.contains("Hello from Inet"));
            }
        } catch (final IOException e) {
            fail("Test error: " + e.getMessage());
        }
    }

    public void testInvalidLocation() {
        final var inet = new Inet(
            new Location(
                new Text.Of("/"),
                new Text.Of("invalid.host.that.does.not.exist"),
                new Num.Of(9999)
            )
        );
        try {
            final var s = inet.socket();
            s.close();
            fail("Should throw InvariantViolation");
        } catch (final IOException e) {
            fail("Should throw InvariantViolation");
        } catch (final InvariantViolation e) {
            assertTrue(e.getMessage().contains("There is no socket"));
        }
    }
}

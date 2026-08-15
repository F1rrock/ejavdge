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
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

public final class InetIT extends TestCase {
    public void testWorkingSocket() {
        try (final var server = new ServerSocket(0)) {
            final var error = new AtomicReference<Exception>();
            final int port = server.getLocalPort();
            final var responseBody = "Hello from Inet";
            final var contentLength = responseBody.getBytes(StandardCharsets.UTF_8).length;
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
                        """
                        HTTP/1.1 200 OK\r
                        Content-Length: %d\r
                        \r
                        %s
                        """.formatted(
                            contentLength,
                            responseBody
                        ).getBytes(StandardCharsets.UTF_8)
                    );
                    out.flush();
                } catch (final IOException e) {
                    error.set(e);
                }
            }).start();
            final var inet = new Inet(
                new Location(
                    new Text.Of("/test"),
                    new Text.Of("localhost"),
                    new Num.Of(port)
                )
            );
            try (final Socket socket = inet.socket()) {
                final OutputStream out = socket.getOutputStream();
                out.write("GET / HTTP/1.1\r\nHost: localhost\r\n\r\n".getBytes(StandardCharsets.UTF_8));
                out.flush();
                final var r = responseOf(socket);
                if (error.get() != null) {
                    fail(error.get().getMessage());
                }
                assertTrue(r.contains("200 OK"));
                assertTrue(r.contains(responseBody));
            }
        } catch (final Exception e) {
            fail(e.getMessage());
        }
    }

    private static String responseOf(Socket socket) throws IOException {
        final InputStream in = socket.getInputStream();
        final var response = new ByteArrayOutputStream();
        final byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            response.write(buffer, 0, bytesRead);
            if (response.toString().contains("\r\n\r\n")) {
                break;
            }
        }
        return response.toString(StandardCharsets.UTF_8);
    }


    public void testInvalidLocation() {
        final var inet = new Inet(
            new Location(
                new Text.Of("/"),
                new Text.Of("localhost"),
                new Num.Of(9999)
            )
        );
        try {
            inet.socket().close();
        } catch (final InvariantViolation e) {
            return;
        } catch (final IOException e) {
            fail(e.getMessage());
        }
        fail("InvariantViolation");
    }
}

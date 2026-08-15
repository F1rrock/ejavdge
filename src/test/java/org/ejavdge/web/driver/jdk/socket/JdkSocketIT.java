package org.ejavdge.web.driver.jdk.socket;

import junit.framework.TestCase;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.spec.HttpSpec;
import org.ejavdge.web.spec.Request;

import java.io.*;
import java.net.ServerSocket;
import java.nio.charset.StandardCharsets;

public final class JdkSocketIT extends TestCase {
    public void testResponseWithContentLength() {
        try (final var server = new ServerSocket(0)) {
            final int port = server.getLocalPort();
            final var responseBody = "Hello";
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
                    fail(e.getMessage());
                }
            }).start();
            assertTrue(response(port).contains("200 OK"));
        } catch (final IOException e) {
            fail(e.getMessage());
        }
    }

    public void testConnectionRefused() {
        final var driver = new JdkSocket();
        final var loc = new Location(
            new Text.Of("/"),
            new Text.Of("localhost"),
            new Num.Of(9999)
        );
        final var req = new Request(
            new HttpSpec.Of("GET / HTTP/1.1\r\nHost: localhost\r\n".getBytes())
        );
        try {
            driver.resourceOf(loc, req);
        } catch (final InvariantViolation e) {
            return;
        }
        fail("InvariantViolation");
    }

    public void testChunkedResponse() {
        try (final var server = new ServerSocket(0)) {
            final int port = server.getLocalPort();
            new Thread(() -> {
                try (final var client = server.accept()) {
                    final InputStream in = client.getInputStream();
                    final var buffer = new byte[1024];
                    while (in.read(buffer) != -1) {
                        if (new String(buffer).contains("\r\n\r\n")) {
                            break;
                        }
                    }
                    final OutputStream out = client.getOutputStream();
                    out.write("""
                        HTTP/1.1 200 OK\r
                        Transfer-Encoding: chunked\r
                        \r
                        5\r
                        Hello\r
                        0\r
                        """.getBytes(StandardCharsets.UTF_8));
                    out.flush();
                } catch (final IOException e) {
                    fail("Server error: " + e.getMessage());
                }
            }).start();
            final var driver = new JdkSocket();
            final var loc = new Location(
                new Text.Of("/"),
                new Text.Of("localhost"),
                new Num.Of(port)
            );
            final var req = new Request(
                new HttpSpec.Of("GET / HTTP/1.1\r\nHost: localhost\r\n".getBytes())
            );
            assertEquals(
                """
                HTTP/1.1 200 OK\r
                Transfer-Encoding: chunked\r
                \r
                Hello""",
                new String(
                    driver.resourceOf(loc, req),
                    StandardCharsets.UTF_8
                )
            );
        } catch (final IOException e) {
            fail("Test error: " + e.getMessage());
        }
    }

    public void testUnsupportedResponse() {
        try (final var server = new ServerSocket(0)) {
            final int port = server.getLocalPort();
            new Thread(() -> {
                try (final var client = server.accept()) {
                    final InputStream in = client.getInputStream();
                    final var buffer = new byte[1024];
                    while (in.read(buffer) != -1) {
                        if (new String(buffer).contains("\r\n\r\n")) {
                            break;
                        }
                    }
                    final OutputStream out = client.getOutputStream();
                    out.write("""
                        HTTP/1.1 200 OK\r
                        \r
                        Hello""".getBytes(StandardCharsets.UTF_8)
                    );
                    out.flush();
                } catch (final IOException e) {
                    fail("Server error: " + e.getMessage());
                }
            }).start();
            final var driver = new JdkSocket();
            final var loc = new Location(
                new Text.Of("/"),
                new Text.Of("localhost"),
                new Num.Of(port)
            );
            final var req = new Request(
                new HttpSpec.Of("GET / HTTP/1.1\r\nHost: localhost\r\n".getBytes())
            );
            try {
                driver.resourceOf(loc, req);
            } catch (final InvariantViolation e) {
                return;
            }
            fail("InvariantViolation");
        } catch (final IOException e) {
            fail("Test error: " + e.getMessage());
        }
    }

    private static String response(int port) {
        final JdkSocket driver = new JdkSocket();
        final Location loc = new Location(
            new Text.Of("/"),
            new Text.Of("localhost"),
            new Num.Of(port)
        );
        final Request req = new Request(
            new HttpSpec.Of(
                "GET / HTTP/1.1\r\nHost: localhost\r\n"
                    .getBytes(StandardCharsets.UTF_8)
            )
        );
        final byte[] response = driver.resourceOf(loc, req);
        return new String(response, StandardCharsets.UTF_8);
    }
}

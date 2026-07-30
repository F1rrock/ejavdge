package org.ejavdge.web.driver.jdk.socket;

import junit.framework.TestCase;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.spec.HttpSpec;
import org.ejavdge.web.spec.Request;

import java.io.*;
import java.net.ServerSocket;

public final class JdkSocketIT extends TestCase {
    public void testConnection() {
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
                    out.write("HTTP/1.1 200 OK\r\n\r\nHello".getBytes());
                    out.flush();
                } catch (final IOException e) {
                    fail(e.getMessage());
                }
            }).start();
            final var r = response(port);
            assertTrue(r.contains("200 OK"));
            assertTrue(r.contains("Hello"));
        } catch (final IOException e) {
            fail(e.getMessage());
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
                new HttpSpec.Of("GET / HTTP/1.1\r\nHost: localhost\r\n".getBytes())
        );
        final byte[] response = driver.resourceOf(loc, req);
        return new String(response, java.nio.charset.StandardCharsets.US_ASCII);
    }
}

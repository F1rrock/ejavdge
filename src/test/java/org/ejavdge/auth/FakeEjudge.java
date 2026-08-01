package org.ejavdge.auth;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public final class FakeEjudge implements AutoCloseable {
    private final Text login;
    private final Text pass;
    private final Num contest;
    private HttpServer server;
    private int port;

    public FakeEjudge() {
        this(
            new Text.Of("login"),
            new Text.Of("pass"),
            new Num.Of(1)
        );
    }

    public FakeEjudge(final Text l, final Text p, final Num c) {
        this.login = l;
        this.pass = p;
        this.contest = c;
    }

    public void start() throws IOException {
        this.server = HttpServer.create(new InetSocketAddress(0), 0);
        this.server.createContext("/", new AuthHandler());
        this.server.setExecutor(null);
        this.server.start();
        this.port = server.getAddress().getPort();
    }

    @Override
    public void close() {
        if (server != null) {
            server.stop(0);
        }
    }

    public int port() {
        return this.port;
    }

    private final class AuthHandler implements HttpHandler {
        @Override
        public void handle(final HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            if (!"POST".equals(method)) {
                sendResponse(exchange, 405, "Method Not Allowed");
                return;
            }
            Map<String, String> params = parseFormData(exchange);
            String action = params.get("action_2");
            String userLogin = params.get("login");
            String userPass = params.get("password");
            String userContest = params.get("contest_id");
            boolean valid = "Log%20in".equals(action)
                && login.content().equals(userLogin)
                && pass.content().equals(userPass)
                && String.valueOf(contest.value()).equals(userContest);
            if (valid) {
                String response = "Welcome to ejudge!";
                sendResponse(exchange, 200, response);
            } else {
                sendResponse(exchange, 403, "Invalid credentials");
            }
            exchange.close();
        }

        private Map<String, String> parseFormData(
            final HttpExchange exchange
        ) throws IOException {
            Map<String, String> result = new HashMap<>();
            String body = new String(
                exchange.getRequestBody().readAllBytes(),
                StandardCharsets.UTF_8
            );
            for (String pair : body.split("&")) {
                String[] parts = pair.split("=");
                if (parts.length == 2) {
                    result.put(parts[0], parts[1]);
                }
            }
            return result;
        }

        private void sendResponse(
            final HttpExchange exchange,
            final int status,
            final String body
        ) throws IOException {
            byte[] response = body.getBytes(StandardCharsets.UTF_8);
            exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
            exchange.getResponseHeaders().set("Connection", "close");
            exchange.sendResponseHeaders(status, response.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response);
                os.flush();
            }
        }
    }
}

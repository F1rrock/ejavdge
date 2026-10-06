package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.text.Text;

import java.net.URI;

/**
 * A {@link Num} that extracts the port number from a URL, applying the default
 * port for the scheme when no explicit port is specified.
 * <p>
 * This class wraps a {@link Url} (or a text that can be interpreted as one) and
 * exposes the port component of the URL as a number. The extraction is
 * performed by parsing the URL with {@link URI#create(String)} and reading its
 * {@link URI#getPort()} value. When the URL does not contain an explicit port
 * (in which case {@code getPort()} returns {@code -1}), the default port for
 * the URL's scheme is used instead:
 * <ul>
 *   <li>{@code 443} for {@code https};</li>
 *   <li>{@code 80} for any other scheme (including {@code http}).</li>
 * </ul>
 * <p>
 * The extraction is wrapped in a {@link NumAbout} with the subject
 * {@code "port of url"}, so any failure is reported with a meaningful message.
 * If the URL is not a valid URI, an {@link InvariantViolation} with the message
 * {@code "There is no legal url"} thrown, wrapping the underlying
 * {@link IllegalArgumentException}.
 * <p>
 * This class used, among other places, by
 * {@link org.ejavdge.web.driver.jdk.socket.Inet} to determine the target port
 * when opening a socket connection.
 */
public final class PortOf implements Num {

    /**
     * The URL from which the port is extracted.
     */
    private final Url url;

    /**
     * Creates a port extractor from the given text, interpreting it as a URL.
     *
     * @param u the text to interpret as a URL
     */
    public PortOf(final Text u) {
        this(new Url(u));
    }

    /**
     * Creates a port extractor from the given URL.
     *
     * @param u the URL from which the port is extracted
     */
    public PortOf(final Url u) {
        this.url = u;
    }

    /**
     * Returns the port number of the underlying URL, or the default port for
     * the URL's scheme if no explicit port is present.
     * <p>
     * The URL content is materialized via {@link Url#content()} and parsed
     * using {@link URI#create(String)}. If the parsed URI specifies a port, that
     * port is returned. Otherwise, {@code 443} is returned for the
     * {@code https} scheme, and {@code 80} for all other schemes. If the URL
     * cannot be parsed as a valid URI, an {@link InvariantViolation} with the
     * message {@code "There is no legal url"} is thrown, wrapping the
     * underlying {@link IllegalArgumentException}.
     *
     * @return the port number of the URL, or the default port for its scheme
     * @throws InvariantViolation if the URL is not a valid URI
     */
    @Override
    public int value() throws InvariantViolation {
        return new NumAbout(
            "port of url",
            () -> {
                try {
                    final var uri = URI.create(this.url.content());
                    final var p = uri.getPort();
                    if (p >= 0) {
                        return p;
                    }
                    return "https".equals(uri.getScheme()) ? 443 : 80;
                } catch (final IllegalArgumentException e) {
                    throw new InvariantViolation("There is no legal url", e);
                }
            }
        ).value();
    }
}

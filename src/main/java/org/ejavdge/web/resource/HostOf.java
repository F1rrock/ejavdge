package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;

import java.net.URI;

/**
 * A {@link Text} that extracts the host component from a URL.
 * <p>
 * This class wraps a {@link Url} (or a text that can be interpreted as one) and
 * exposes only the host portion of the URL — for example, {@code example.com}
 * from {@code https://example.com/path}. The extraction is performed by parsing
 * the URL with {@link URI#create(String)} and reading its
 * {@link URI#getHost()} value.
 * <p>
 * The extraction is wrapped in a {@link TextAbout} with the subject
 * {@code "host of url"}, so any failure is reported with a meaningful message:
 * <ul>
 *   <li>if the URL is not a valid URI, an {@link InvariantViolation} with the
 *       message {@code "There is no legal url"} is thrown, wrapping the
 *       underlying {@link IllegalArgumentException};</li>
 *   <li>if the URL is valid but has no host component (for example, a relative
 *       or opaque URI), an {@link InvariantViolation} with the message
 *       {@code "There is no host in url"} is thrown.</li>
 * </ul>
 * <p>
 * This class is used, among other places, by
 * {@link org.ejavdge.web.driver.jdk.socket.Inet} to determine the target host
 * when opening a socket connection.
 */
public final class HostOf implements Text {

    /**
     * The URL from which the host is extracted.
     */
    private final Url url;

    /**
     * Creates a host extractor from the given text, interpreting it as a URL.
     *
     * @param u the text to interpret as a URL
     */
    public HostOf(final Text u) {
        this(new Url(u));
    }

    /**
     * Creates a host extractor from the given URL.
     *
     * @param u the URL from which the host is extracted
     */
    public HostOf(final Url u) {
        this.url = u;
    }

    /**
     * Returns the host component of the underlying URL.
     * <p>
     * The URL content is materialized via {@link Url#content()} and parsed
     * using {@link URI#create(String)}. The resulting host is returned. If the
     * URL cannot be parsed as a valid URI, or if it does not contain a host
     * component, an {@link InvariantViolation} is thrown with a descriptive
     * message.
     *
     * @return the host component of the URL
     * @throws InvariantViolation if the URL is not a valid URI or does not
     *         contain a host component
     */
    @Override
    public String content() throws InvariantViolation {
        return new TextAbout(
            "host of url",
            () -> {
                try {
                    final var host =  URI.create(this.url.content()).getHost();
                    if (host == null) {
                        throw new InvariantViolation("There is no host in url");
                    }
                    return host;
                } catch (final IllegalArgumentException e) {
                    throw new InvariantViolation("There is no legal url", e);
                }
            }
        ).content();
    }
}

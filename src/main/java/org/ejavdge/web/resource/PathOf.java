package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.*;

import java.net.URI;

/**
 * A {@link Text} that extracts the path component from a URL.
 * <p>
 * This class wraps a {@link Url} (or a text that can be interpreted as one) and
 * exposes only the path portion of the URL — for example, {@code /contest/42/}
 * from {@code https://example.com/contest/42/}. The extraction is performed by
 * parsing the URL with {@link URI#create(String)} and reading its
 * {@link URI#getPath()} value.
 * <p>
 * The extraction is wrapped in a {@link TextAbout} with the subject
 * {@code "path of url"}, so any failure is reported with a meaningful message:
 * <ul>
 *   <li>if the URL is not a valid URI, an {@link InvariantViolation} with the
 *       message {@code "There is no legal url"} is thrown, wrapping the
 *       underlying {@link IllegalArgumentException};</li>
 *   <li>if the URL is valid but has no path component, an
 *       {@link InvariantViolation} with the message
 *       {@code "There is no path in url"} is thrown.</li>
 * </ul>
 * <p>
 * In addition, if the extracted path is empty, it is replaced with a single
 * {@code "/"} character via a {@link Fallback} combined with
 * {@link NonEmpty}. This ensures that a URL without an explicit path (such as
 * {@code https://example.com}) is treated as having the root path {@code /},
 * which is the convention used by HTTP.
 * <p>
 * This class is used, among other places, by
 * {@link org.ejavdge.file.DownloadingOf} and {@link LastSegmentOf} to derive
 * file names and locations from attachment URLs.
 */
public final class PathOf implements Text {

    /**
     * The URL from which the path is extracted.
     */
    private final Url url;

    /**
     * Creates a path extractor from the given text, interpreting it as a URL.
     *
     * @param u the text to interpret as a URL
     */
    public PathOf(final Text u) {
        this(new Url(u));
    }

    /**
     * Creates a path extractor from the given URL.
     *
     * @param u the URL from which the path is extracted
     */
    public PathOf(final Url u) {
        this.url = u;
    }

    /**
     * Returns the path component of the underlying URL.
     * <p>
     * The URL content is materialized via {@link Url#content()} and parsed
     * using {@link URI#create(String)}. The resulting path is returned, with
     * the following adjustments:
     * <ul>
     *   <li>if the URL cannot be parsed as a valid URI, an
     *       {@link InvariantViolation} with the message
     *       {@code "There is no legal url"} is thrown;</li>
     *   <li>if the parsed URI has no path component, an
     *       {@link InvariantViolation} with the message
     *       {@code "There is no path in url"} is thrown;</li>
     *   <li>if the extracted path is empty, it is replaced with {@code "/"}
     *       so that a URL without an explicit path is treated as having the
     *       root path.</li>
     * </ul>
     *
     * @return the path component of the URL, or {@code "/"} if the path is
     *         empty
     * @throws InvariantViolation if the URL is not a valid URI or does not
     *         contain a path component
     */
    @Override
    public String content() throws InvariantViolation {
        return new TextAbout(
            "path of url",
            new BindOfText(
                () -> {
                    try {
                        final var path =  URI.create(this.url.content()).getPath();
                        if (path == null) {
                            throw new InvariantViolation("There is no path in url");
                        }
                        return path;
                    } catch (final IllegalArgumentException e) {
                        throw new InvariantViolation("There is no legal url", e);
                    }
                },
                p -> new Fallback(
                    new NonEmpty(new Text.Of(p)),
                    new Text.Of("/")
                )
            )
        ).content();
    }
}

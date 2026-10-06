package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.num.Positive;
import org.ejavdge.scalar.text.*;
import org.ejavdge.web.media.Media;
import org.ejavdge.web.media.WhiteList;
import org.ejavdge.web.resource.Url;

/**
 * A web context that represents the location of a resource in the contest
 * system, consisting of a URL, a host, and a port.
 * <p>
 * This class implements {@link Context} and supplies three named entries when
 * imprinted onto a {@link Media}: {@code "url"}, {@code "host"}, and
 * {@code "port"}. These values are typically used to construct requests to the
 * contest system.
 * <p>
 * A location can be created from a base URL, a host, and a port, or derived
 * from an existing location by appending additional query parameters via a
 * {@link Context}. The host and port are wrapped with
 * {@link TextAbout} and {@link NumAbout} respectively for diagnostic purposes,
 * and the port is validated to be strictly positive via {@link Positive}.
 * <p>
 * Two inner classes, {@link Host} and {@link Port}, provide focused contexts
 * that imprint only the host or only the port component of a location. They
 * are useful when only one of these values is needed, for example when
 * constructing a {@link org.ejavdge.web.context.Location} for a different
 * resource.
 */
public final class Location implements Context {

    /**
     * The URL of the location.
     */
    private final Url url;

    /**
     * The host of the location, wrapped for diagnostics.
     */
    private final Text host;

    /**
     * The port of the location, wrapped and validated as positive.
     */
    private final Num port;

    /**
     * Creates a new location by copying the host and port from an existing
     * location and constructing a new URL that combines the existing URL with
     * the given query context.
     * <p>
     * This constructor is useful for deriving a location that points to a
     * different resource on the same host and port, with additional query
     * parameters supplied by the {@code query} context.
     *
     * @param loc   the original location from which host and port are taken
     * @param query the additional query context to append to the URL
     */
    public Location(final Location loc, final Context query) {
        this.url = new Url(loc.url, query);
        this.host = loc.host;
        this.port = loc.port;
    }

    /**
     * Creates a new location from the given base URL text, host, and port.
     * <p>
     * The host is wrapped in {@link TextAbout} with the subject {@code "host"},
     * and the port is wrapped in {@link NumAbout} with the subject
     * {@code "port"} and validated to be strictly positive via
     * {@link Positive}.
     *
     * @param base the base URL text
     * @param host the host name
     * @param port the port number, must be strictly positive
     */
    public Location(final Text base, final Text host, final Num port) {
        this.url = new Url(base);
        this.host = new TextAbout("host", host);
        this.port = new NumAbout("port", new Positive(port));
    }

    /**
     * Imprints this location onto the given media.
     * <p>
     * The URL, host, and port are attached to the media as entries named
     * {@code "url"}, {@code "host"}, and {@code "port"} respectively. The port
     * is converted from its numeric form to text before being attached.
     *
     * @param <T> the type of the result produced by the imprint operation
     * @param m   the media to imprint this location onto
     * @return the result of imprinting the location onto the media
     * @throws InvariantViolation if an invariant is violated during the imprint
     *         operation, for example if the port is not positive
     */
    @Override
    public <T> T imprint(final Media<T> m) throws InvariantViolation {
        return m
            .with(new Text.Of("url"), this.url)
            .with(new Text.Of("host"), this.host)
            .with(new Text.Of("port"), new TextOfNum(this.port))
            .content();
    }

    /**
     * A context that imprints only the host component of a {@link Location}.
     * <p>
     * This class is useful when only the host value is needed, for example when
     * constructing a location for a different resource on the same host. It
     * achieves this by filtering the media to only the {@code "host"} entry
     * using a {@link WhiteList} before delegating to the location's
     * {@link Location#imprint(Media)} method.
     */
    public static final class Host implements Context {

        /**
         * The location from which the host is extracted.
         */
        private final Location loc;

        /**
         * Creates a host context from the given location.
         *
         * @param loc the location whose host will be imprinted
         */
        public Host(final Location loc) {
            this.loc = loc;
        }

        /**
         * Imprints only the host component of the location onto the given
         * media.
         * <p>
         * The media is first wrapped in a {@link WhiteList} that restricts it
         * to the {@code "host"} entry, and then the location's
         * {@link Location#imprint(Media)} method is invoked. This ensures that
         * only the host value is attached to the resulting media.
         *
         * @param <T> the type of the result produced by the imprint operation
         * @param m   the media to imprint the host onto
         * @return the result of imprinting only the host onto the media
         * @throws InvariantViolation if an invariant is violated during the
         *         imprint operation
         */
        @Override
        public <T> T imprint(final Media<T> m) throws InvariantViolation {
            return this.loc.imprint(
                new WhiteList<>(
                    new Text.Of("host"),
                    m
                )
            );
        }
    }

    /**
     * A context that imprints only the port component of a {@link Location}.
     * <p>
     * This class is useful when only the port value is needed, for example when
     * constructing a location for a different resource on the same port. It
     * achieves this by filtering the media to only the {@code "port"} entry
     * using a {@link WhiteList} before delegating to the location's
     * {@link Location#imprint(Media)} method.
     */
    public static final class Port implements Context {

        /**
         * The location from which the port is extracted.
         */
        private final Location loc;

        /**
         * Creates a port context from the given location.
         *
         * @param loc the location whose port will be imprinted
         */
        public Port(final Location loc) {
            this.loc = loc;
        }

        /**
         * Imprints only the port component of the location onto the given
         * media.
         * <p>
         * The media is first wrapped in a {@link WhiteList} that restricts it
         * to the {@code "port"} entry, and then the location's
         * {@link Location#imprint(Media)} method is invoked. This ensures that
         * only the port value is attached to the resulting media.
         *
         * @param <T> the type of the result produced by the imprint operation
         * @param m   the media to imprint the port onto
         * @return the result of imprinting only the port onto the media
         * @throws InvariantViolation if an invariant is violated during the
         *         imprint operation
         */
        @Override
        public <T> T imprint(final Media<T> m) throws InvariantViolation {
            return this.loc.imprint(
                new WhiteList<>(
                    new Text.Of("port"),
                    m
                )
            );
        }
    }
}

package org.ejavdge.web.spec.method;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Map;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.NonEmpty;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.media.Gist;
import org.ejavdge.web.spec.HttpSpec;

/**
 * An {@link HttpSpec} representing the opening line and required headers of
 * an HTTP {@code GET} request.
 * <p>
 * The produced message consists of:
 * <ul>
 *   <li>the request line {@code GET <url> HTTP/1.1};</li>
 *   <li>a {@code Host} header in the form {@code Host: <host>:<port>}.</li>
 * </ul>
 * The values for the URL, host, and port are extracted from a
 * {@link Location} via {@link Gist.ImprintOf}, which yields them in order
 * and wraps each in {@link NonEmpty} so that empty components are reported
 * as errors rather than silently producing a malformed request line.
 * <p>
 * This spec provides only the first line of the request and the
 * {@code Host} header. Additional headers, a body, or a terminator can be
 * attached with the decorators in {@link org.ejavdge.web.spec.header} and
 * {@link org.ejavdge.web.spec.body}, and the resulting message is
 * materialized as raw bytes via {@link #bytes()}.
 */
public final class Get implements HttpSpec {

    /**
     * The underlying byte content of the request opening.
     */
    final Bytes src;

    /**
     * Creates a GET request opening from the given URL path, host, and
     * port.
     * <p>
     * This is a convenience constructor that assembles a
     * {@link Location} from the three components and delegates to the
     * {@link #Get(Location)} constructor.
     *
     * @param u the URL path of the target resource
     * @param h the host name of the target server
     * @param p the port number of the target server
     */
    public Get(final Text u, final Text h, final Num p) {
        this(new Location(u, h, p));
    }

    /**
     * Creates a GET request opening from the given location.
     * <p>
     * The location's URL path, host, and port are materialized and
     * substituted into a stencil that produces the request line and the
     * {@code Host} header. Each component is required to be non-empty
     * via {@link NonEmpty}; if any of them is empty, an
     * {@link InvariantViolation} is thrown when the request is
     * materialized.
     *
     * @param loc the location of the target resource
     */
    public Get(final Location loc) {
        this.src = new Utf8(
            new Stencil(
                new Text.Of(
                    """
                    GET %s HTTP/1.1\r
                    Host: %s:%s\r
                    """
                ),
                new Map<>(
                    NonEmpty::new,
                    new Gist.ImprintOf(loc)
                )
            )
        );
    }

    /**
     * Returns the raw bytes of this HTTP request opening, including the
     * request line and the {@code Host} header.
     *
     * @return the HTTP message as a byte array
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the message, for example if the URL, host, or
     *         port is empty
     */
    @Override
    public byte[] bytes() throws InvariantViolation {
        return this.src.content();
    }
}

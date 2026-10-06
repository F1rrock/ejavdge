package org.ejavdge.contest;

import org.ejavdge.auth.Session;
import org.ejavdge.effect.Envelope;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Joint;
import org.ejavdge.scalar.bytes.BindOfBytes;
import org.ejavdge.scalar.bytes.BytesAbout;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.ContextOfEjsid;
import org.ejavdge.web.context.ContextOfSid;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.media.Cookies;
import org.ejavdge.web.resource.HasStatus;
import org.ejavdge.web.resource.WebResource;
import org.ejavdge.web.spec.Request;
import org.ejavdge.web.spec.body.multipart.Multipart;
import org.ejavdge.web.spec.body.multipart.Part;
import org.ejavdge.web.spec.body.multipart.TextParts;
import org.ejavdge.web.spec.header.Header;
import org.ejavdge.web.spec.header.WithHeaders;
import org.ejavdge.web.spec.method.Post;

/**
 * A form that can be submitted to the ejudge contest system.
 * <p>
 * This class encapsulates a web driver, a location, an authenticated session,
 * and a collection of multipart parts representing the form fields. It
 * implements {@link Envelope} to provide a {@link #send()} method that submits
 * the form via a multipart POST request.
 * <p>
 * Forms can be constructed either from an existing form (copying its driver,
 * location, and session while extending its fields) or from scratch with an
 * empty set of fields.
 */
public final class ContestForm implements Envelope {

    /**
     * The web driver used to execute the request.
     */
    private final WebDriver driver;

    /**
     * The location of the contest system.
     */
    private final Location location;

    /**
     * The authenticated session used for the request.
     */
    private final Session session;

    /**
     * The multipart parts representing the form fields.
     */
    private final Items<Part> fields;

    /**
     * Creates a new form by copying the driver, location, and session from an
     * existing form and appending the given parts to its fields.
     * <p>
     * The resulting form's fields are a joint collection of the existing
     * form's fields and the provided parts.
     *
     * @param cf the existing form to copy the driver, location, and session from
     * @param ps the additional multipart parts to append to the form fields
     */
    public ContestForm(final ContestForm cf, final Items<Part> ps) {
        this.driver = cf.driver;
        this.location = cf.location;
        this.session = cf.session;
        this.fields = new Joint<>(cf.fields, ps);
    }

    /**
     * Creates a new form with the given driver, location, and session, and an
     * empty collection of fields.
     *
     * @param d the web driver used to execute the request
     * @param l the location of the contest system
     * @param s the authenticated session used for the request
     */
    public ContestForm(final WebDriver d, final Location l, final Session s) {
        this.driver = d;
        this.location = l;
        this.session = s;
        this.fields = new Items.Of<>();
    }

    /**
     * Submits this form to the contest system.
     * <p>
     * The form is sent as a multipart POST request. The request includes the
     * session ID ({@code sid}) as a text part and the session's {@code ejsid}
     * cookie. The response is expected to have HTTP status 302 (redirect);
     * otherwise, an error message is produced.
     * <p>
     * The request is wrapped with several decorators:
     * <ul>
     *   <li>{@link BytesAbout} – attaches a descriptive label to the bytes;</li>
     *   <li>{@link BindOfBytes} – binds the session bytes to the request;</li>
     *   <li>{@link HasStatus} – verifies the expected status code (302) and
     *       provides an error message if the status is different.</li>
     * </ul>
     *
     * @throws InvariantViolation if an invariant is violated during submission,
     *         for example if the response status is not 302
     */
    @Override
    public void send() throws InvariantViolation {
        new BytesAbout(
            "contest form",
            new BindOfBytes(
                this.session,
                s -> new HasStatus(
                    new Num.Of(302),
                    new Text.Of("There is a problem with sending."),
                    new WebResource(
                        this.driver,
                        this.location,
                        new Request(
                            new Multipart(
                                new Joint<>(
                                    new TextParts.ImprintOf(
                                        new ContextOfSid(s)
                                    ),
                                    this.fields
                                ),
                                new WithHeaders(
                                    new Header(
                                        new Text.Of("Cookie"),
                                        new Cookies.ImprintOf(
                                            new ContextOfEjsid(s)
                                        )
                                    ),
                                    new Post(this.location)
                                )
                            )
                        )
                    )
                )
            )
        ).content();
    }
}

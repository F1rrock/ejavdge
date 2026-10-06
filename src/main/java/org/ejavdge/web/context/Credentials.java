package org.ejavdge.web.context;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.num.Positive;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;
import org.ejavdge.scalar.text.TextOfNum;
import org.ejavdge.web.media.Media;

/**
 * A web context that supplies the credentials required to authenticate against
 * the ejudge contest system.
 * <p>
 * This class implements {@link Context} and holds three pieces of information
 * needed for login: the user's login, the user's password, and the contest
 * identifier. When imprinted onto a {@link Media}, these values are attached as
 * named entries — {@code "login"}, {@code "password"}, and {@code "contest_id"}
 * — which are typically used as form fields in a login request.
 * <p>
 * The password and login values are wrapped in {@link TextAbout} so that any
 * failure while materializing them is reported with the appropriate subject
 * ({@code "login"} or {@code "password"}). The contest identifier is wrapped in
 * a {@link NumAbout} with the subject {@code "contest id"} and additionally
 * validated as {@link Positive}, ensuring that only a strictly positive contest
 * number is accepted.
 * <p>
 * Values can be supplied either as plain {@code String} and {@code int}
 * literals, or as {@link Text} and {@link Num} instances, allowing them to be
 * computed lazily if desired.
 */
public final class Credentials implements Context {

    /**
     * The user's login, wrapped for diagnostic purposes.
     */
    private final Text login;

    /**
     * The user's password, wrapped for diagnostic purposes.
     */
    private final Text pass;

    /**
     * The contest identifier, wrapped and validated as positive.
     */
    private final Num contest;

    /**
     * Creates credentials from the given login, password, and contest
     * identifier.
     *
     * @param l the user's login
     * @param p the user's password
     * @param c the contest identifier, must be strictly positive
     */
    public Credentials(final String l, final String p, final int c) {
        this(new Text.Of(l), new Text.Of(p), new Num.Of(c));
    }

    /**
     * Creates credentials from the given login, password, and contest
     * identifier.
     * <p>
     * The login and password are wrapped in {@link TextAbout} with the subjects
     * {@code "login"} and {@code "password"} respectively, so that any failure
     * while materializing them is reported with the appropriate label. The
     * contest identifier is wrapped in a {@link NumAbout} with the subject
     * {@code "contest id"} and validated to be strictly positive via
     * {@link Positive}.
     *
     * @param l the user's login
     * @param p the user's password
     * @param c the contest identifier, must be strictly positive
     */
    public Credentials(final Text l, final Text p, final Num c) {
        this.login = new TextAbout("login", l);
        this.pass = new TextAbout("password", p);
        this.contest = new NumAbout("contest id", new Positive(c));
    }

    /**
     * Imprints these credentials onto the given media.
     * <p>
     * The login, password, and contest identifier are attached to the media as
     * entries named {@code "login"}, {@code "password"}, and
     * {@code "contest_id"} respectively. The contest identifier is converted
     * from its numeric form to text before being attached.
     *
     * @param <T> the type of the result produced by the imprint operation
     * @param m   the media to imprint these credentials onto
     * @return the result of imprinting the credentials onto the media
     * @throws InvariantViolation if an invariant is violated during the imprint
     *         operation, for example if the contest identifier is not positive
     */
    @Override
    public <T> T imprint(final Media<T> m) throws InvariantViolation {
        return m
            .with(new Text.Of("login"), this.login)
            .with(new Text.Of("password"), this.pass)
            .with(new Text.Of("contest_id"), new TextOfNum(this.contest))
            .content();
    }
}

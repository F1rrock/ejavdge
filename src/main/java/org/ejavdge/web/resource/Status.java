package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.num.Num;
import org.ejavdge.scalar.num.NumAbout;
import org.ejavdge.scalar.num.NumOfText;
import org.ejavdge.scalar.num.Positive;
import org.ejavdge.scalar.text.Match;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.Utf8Text;

/**
 * A {@link Num} that represents the HTTP status code of a response.
 * <p>
 * This class extracts the numeric status code from the status line of an HTTP
 * response. The status line has the form {@code HTTP/<major>.<minor> <code> <reason>},
 * for example {@code HTTP/1.1 200 OK}. The extraction is performed by decoding
 * the raw response bytes as UTF-8 text and applying the regular expression
 * {@code (?<=HTTP/\d\.\d )\d{3}}, which matches exactly three digits that
 * follow the protocol version and a space.
 * <p>
 * The extracted code is parsed as a decimal integer via {@link NumOfText} and
 * validated to be strictly positive via {@link Positive}, ensuring that a
 * missing or malformed status code is reported as an error. The value is also
 * labelled with the subject {@code "status"} via {@link NumAbout}, so any
 * failure while materializing the code is reported with a meaningful message.
 * <p>
 * This class used by {@link HasStatus} to verify that a response has the
 * expected status code, and by other parts of the application that need to
 * inspect the HTTP status of a response.
 */
public final class Status implements Num {

    /**
     * The underlying numeric status code, extracted and validated.
     */
    private final Num origin;

    /**
     * Creates a status code extractor from the given raw HTTP response bytes.
     * <p>
     * The response bytes are expected to contain the status line at the very
     * beginning, as is standard for HTTP responses. The status code is extracted
     * using a regular expression and parsed as a positive integer.
     *
     * @param src the raw HTTP response bytes containing the status line
     */
    public Status(final Bytes src) {
        this.origin = new NumAbout(
            "status",
            new Positive(
                new NumOfText(
                    new Match(
                        new Utf8Text(src),
                        new Text.Of(
                            "(?<=HTTP/\\d\\.\\d )\\d{3}"
                        )
                    )
                )
            )
        );
    }

    /**
     * Returns the numeric HTTP status code of the response.
     *
     * @return the HTTP status code as an integer
     * @throws InvariantViolation if the status line is missing, the status code
     *         cannot be parsed, or the extracted value is not positive
     */
    @Override
    public int value() throws InvariantViolation {
        return this.origin.value();
    }
}

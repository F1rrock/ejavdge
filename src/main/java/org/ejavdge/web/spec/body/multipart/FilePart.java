package org.ejavdge.web.spec.body.multipart;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.file.ByteFile;
import org.ejavdge.file.ContentOf;
import org.ejavdge.file.NameOf;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.Concat;
import org.ejavdge.scalar.bytes.Utf8;
import org.ejavdge.scalar.text.NonEmpty;
import org.ejavdge.scalar.text.Stencil;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;

/**
 * A {@link Part} that represents a file inside a
 * {@code multipart/form-data} body.
 * <p>
 * A file part consists of the standard part headers followed by the raw
 * bytes of the file:
 * <ul>
 *   <li>a {@code Content-Disposition} header naming the form field
 *       ({@code file}) and providing the file name via the {@code filename}
 *       parameter;</li>
 *   <li>a {@code Content-Type} header set to
 *       {@code application/octet-stream};</li>
 *   <li>a blank line separating the headers from the body;</li>
 *   <li>the file's content bytes.</li>
 * </ul>
 * <p>
 * The part can be constructed either from a {@link ByteFile} (which
 * supplies both the file name and its content) or from an explicit name
 * and content. The file name is required to be non-empty; passing an empty
 * name results in an {@link InvariantViolation} being thrown when the part
 * is materialized.
 */
public final class FilePart implements Part {

    /**
     * The underlying byte content of the part.
     */
    private final Bytes src;

    /**
     * Creates a file part from the given file, using the file's name as
     * the {@code filename} parameter and the file's content as the part
     * body.
     *
     * @param f the file whose name and content are used
     */
    public FilePart(final ByteFile f) {
        this(new NameOf(f), new ContentOf(f));
    }

    /**
     * Creates a file part with the given file name and content.
     * <p>
     * The name is embedded into the {@code filename} parameter of the
     * {@code Content-Disposition} header. It is required to be non-empty,
     * and an {@link InvariantViolation} is thrown otherwise when the part
     * is materialized.
     *
     * @param n  the name of the file, must be non-empty
     * @param bs the content of the file
     */
    public FilePart(final Text n, final Bytes bs) {
        this(
            new Concat(
                new Utf8(
                    new Stencil(
                        new Text.Of(
                            """
                            Content-Disposition: form-data; name="file"; filename="%s"\r
                            Content-Type: application/octet-stream\r
                            \r
                            """
                        ),
                        new TextAbout(
                            "file name",
                            new NonEmpty(n)
                        )
                    )
                ),
                bs
            )
        );
    }

    /**
     * Creates a file part from precomputed byte content.
     * <p>
     * This constructor is intended for cases where the full part bytes
     * have already been assembled, for example when reconstructing a
     * multipart payload from an existing request.
     *
     * @param bs the raw bytes of the part
     */
    public FilePart(final Bytes bs) {
        this.src = bs;
    }

    /**
     * Returns the raw bytes of this part, including its headers and body.
     *
     * @return the part content as a byte array
     * @throws InvariantViolation if an invariant is violated while
     *         materializing the part, for example if the file name is
     *         empty
     */
    @Override
    public byte[] content() throws InvariantViolation {
        return this.src.content();
    }
}

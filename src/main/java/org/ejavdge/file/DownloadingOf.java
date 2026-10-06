package org.ejavdge.file;

import org.ejavdge.effect.Effect;
import org.ejavdge.error.InvariantViolation;
import org.ejavdge.items.Items;
import org.ejavdge.items.Map;
import org.ejavdge.scalar.bytes.Bytes;
import org.ejavdge.scalar.bytes.BytesAbout;
import org.ejavdge.scalar.bytes.Memo;
import org.ejavdge.scalar.text.*;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.WebDriver;
import org.ejavdge.web.resource.*;
import org.ejavdge.web.spec.Request;
import org.ejavdge.web.spec.method.Get;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * An effect that downloads a collection of files (attachments) from the
 * contest system and saves them to a local directory.
 * <p>
 * This class implements {@link Effect} and represents the action of downloading
 * one or more attachments referenced by URLs and writing them to disk under a
 * specified directory. The target directory is created if it does not already
 * exist, and each attachment is written with the file name derived from the last
 * segment of its URL.
 * <p>
 * For each URL provided:
 * <ol>
 *   <li>The file name is derived from the last segment of the URL path and
 *       validated to be non-empty. If the name cannot be determined, an
 *       {@link InvariantViolation} is thrown with a descriptive message.</li>
 *   <li>The file content is fetched via an HTTP GET request to the given URL,
 *       using the provided {@link WebDriver}, and memoized to avoid repeated
 *       downloads within the same effect.</li>
 *   <li>The content is written to the target directory under the derived file
 *       name.</li>
 * </ol>
 * <p>
 * If any I/O error occurs during directory creation or file writing, an
 * {@link InvariantViolation} is thrown wrapping the underlying
 * {@link IOException}.
 */
public final class DownloadingOf implements Effect {

    /**
     * The local path of the directory into which the attachments are written.
     */
    private final Text path;

    /**
     * The collection of files to download.
     */
    private final Items<ByteFile> files;

    /**
     * Creates a download effect for the given attachment URLs.
     * <p>
     * Each URL is converted into a {@link ByteFile} whose name is derived from
     * the last segment of the URL path and whose content is fetched lazily via
     * an HTTP GET request using the provided driver. The files are written to
     * the given target path when the effect is performed.
     *
     * @param us the URLs of the attachments to download
     * @param d  the web driver used to fetch the attachment contents
     * @param p  the local directory into which the attachments will be written
     */
    public DownloadingOf(final Items<Text> us, final WebDriver d, final Text p) {
        this.path = p;
        this.files = new Map<>(
            u -> new ByteFile.Of(
                new TextAbout(
                    "name of attachment",
                    new NonEmpty(
                        new TextFromUrl(
                            new LastSegmentOf(
                                new PathOf(u)
                            ),
                            new Text.Of("There is no correct name for the attachment.")
                        ),
                        new Text.Of("The name of the attachment is empty.")
                    )
                ),
                new BytesAbout(
                    "contents of attachment",
                    new PayloadOf(
                        new ResourceOf(
                            d,
                            new Location(
                                new PathOf(u),
                                new HostOf(u),
                                new PortOf(u)
                            )
                        )
                    )
                )
            ),
            us
        );
    }

    /**
     * Performs this effect by downloading all attachments and writing them to
     * the target directory.
     * <p>
     * The target directory is created if necessary. For each file, its content
     * is fetched and written to disk under the file's name inside the target
     * directory. If an I/O error occurs, an {@link InvariantViolation} is
     * thrown with the underlying cause.
     *
     * @throws InvariantViolation if the directory cannot be created, a file
     *         cannot be written, or any attachment's name or content cannot be
     *         determined
     */
    @Override
    public void perform() throws InvariantViolation {
        try {
            final var dir = Path.of(this.path.content());
            Files.createDirectories(dir);
            for (final var file : this.files.contents()) {
                Files.write(
                    dir.resolve(file.name()),
                    file.content()
                );
            }
        } catch (final IOException e) {
            throw new InvariantViolation(
                "There is no downloading of attachments", e
            );
        }
    }

    /**
     * A {@link Bytes} implementation that fetches a remote resource via an HTTP
     * GET request.
     * <p>
     * The fetched content is memoized using {@link Memo}, so repeated calls to
     * {@link #content()} do not trigger additional network requests.
     */
    private static final class ResourceOf implements Bytes {

        /**
         * The underlying memoized byte content.
         */
        private final Bytes origin;

        /**
         * Creates a resource that fetches content from the given location using
         * the provided driver.
         *
         * @param d the web driver used to execute the request
         * @param l the location of the resource to fetch
         */
        public ResourceOf(final WebDriver d, final Location l) {
            this.origin = new Memo(
                new WebResource(
                    d, l,
                    new Request(
                        new Get(l)
                    )
                )
            );
        }

        /**
         * Returns the raw content of the fetched resource.
         *
         * @return the resource content as a byte array
         * @throws InvariantViolation if an invariant is violated while fetching
         *         the content
         */
        @Override
        public byte[] content() throws InvariantViolation {
            return this.origin.content();
        }
    }
}

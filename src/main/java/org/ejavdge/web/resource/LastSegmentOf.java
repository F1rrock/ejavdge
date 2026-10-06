package org.ejavdge.web.resource;

import org.ejavdge.error.InvariantViolation;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.scalar.text.TextAbout;

/**
 * A {@link Text} that extracts the last segment from a URL path.
 * <p>
 * This class wraps a {@link PathOf} (which provides the path component of a
 * URL) and exposes only the final segment — that is, the part of the path
 * after the last {@code '/'} character. For example, given the path
 * {@code /contest/42/problems/hello.pdf}, this class would return
 * {@code hello.pdf}.
 * <p>
 * The extraction is performed by locating the last {@code '/'} in the path and
 * returning everything that follows it. If the path does not contain any
 * {@code '/'} character, an {@link InvariantViolation} is thrown with a message
 * indicating that the path has no segments.
 * <p>
 * The extraction is wrapped in a {@link TextAbout} with the subject
 * {@code "last segment of path"}, so any failure is reported with a meaningful
 * message.
 * <p>
 * This class is used, among other places, by
 * {@link org.ejavdge.file.DownloadingOf} to derive the file name of a remote
 * attachment from its URL.
 */
public final class LastSegmentOf implements Text {

    /**
     * The path from which the last segment is extracted.
     */
    private final PathOf path;

    /**
     * Creates a last-segment extractor over the given path.
     *
     * @param p the path whose last segment is exposed
     */
    public LastSegmentOf(final PathOf p) {
        this.path = p;
    }

    /**
     * Returns the last segment of the underlying path.
     * <p>
     * The path content is materialized via {@link PathOf#content()}, and the
     * portion of the string after the last {@code '/'} character is returned.
     * If the path contains no {@code '/'}, an {@link InvariantViolation} is
     * thrown with a message that includes the offending path.
     *
     * @return the last segment of the path
     * @throws InvariantViolation if the path does not contain any {@code '/'}
     *         character, or if an invariant is violated while materializing the
     *         path
     */
    @Override
    public String content() throws InvariantViolation {
        return new TextAbout(
            "last segment of path",
            () -> {
                final var p = this.path.content();
                final var slash = p.lastIndexOf('/');
                if (slash < 0) {
                    throw new InvariantViolation(
                        "There is no segments in path: " + p
                    );
                }
                return p.substring(slash + 1);
            }
        ).content();
    }
}

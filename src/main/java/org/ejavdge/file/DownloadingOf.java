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

public final class DownloadingOf implements Effect {
    private final Text path;
    private final Items<ByteFile> files;

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

    private static final class ResourceOf implements Bytes {
        private final Bytes origin;

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

        @Override
        public byte[] content() throws InvariantViolation {
            return this.origin.content();
        }
    }
}

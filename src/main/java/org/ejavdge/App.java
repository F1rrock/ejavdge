package org.ejavdge;

import org.ejavdge.auth.Session;
import org.ejavdge.page.ContestPage;
import org.ejavdge.scalar.num.NumOfText;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.jdk.socket.JdkSocket;

@SuppressWarnings("java:S106")
public final class App
{
    public static void main(final String[] args)
    {
        final var loc = new Location(
            new Text.Of(args[0]),
            new Text.Of(args[1]),
            new NumOfText(args[2])
        );
        System.out.println(
            new ContestPage(
                new JdkSocket(),
                loc,
                new Session(
                    new JdkSocket(),
                    loc,
                    new Credentials(
                        new Text.Of(args[3]),
                        new Text.Of(args[4]),
                        new NumOfText(args[5])
                    )
                )
            ).content()
        );
    }
}

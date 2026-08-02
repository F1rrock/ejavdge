package org.ejavdge;

import org.ejavdge.auth.LoginReply;
import org.ejavdge.scalar.num.NumOfText;
import org.ejavdge.scalar.text.Text;
import org.ejavdge.web.context.Credentials;
import org.ejavdge.web.context.Location;
import org.ejavdge.web.driver.jdk.socket.JdkSocket;

import java.nio.charset.StandardCharsets;

@SuppressWarnings("java:S106")
public final class App
{
    public static void main(final String[] args)
    {
        System.out.println(
            new String(
                new LoginReply(
                    new JdkSocket(),
                    new Location(
                        new Text.Of(args[0]),
                        new Text.Of(args[1]),
                        new NumOfText(args[2])
                    ),
                    new Credentials(
                        new Text.Of(args[3]),
                        new Text.Of(args[4]),
                        new NumOfText(args[5])
                    )
                ).content(),
                StandardCharsets.UTF_8
            )
        );
    }
}

# EJavdge
EJavdge is a Java client library for working with ejudge contests. It provides
application actions for browsing problems, running Java solutions against sample
tests, submitting source files, and retrieving judging reports.

## Requirements
- JDK 16 or newer.
- Apache Maven, available as `mvn`.
- A compatible ejudge contest and account for actions that access the server.
- The `java` executable on `PATH` for local solution execution.

## Features
| Action | Purpose |
| --- | --- |
| `AvailableProblems` | List problems available in the contest. |
| `AlreadySolved` | List problems the current user has solved. |
| `ProblemDescription` | Read a problem statement and its references. |
| `AttachmentsDownload` | Download a problem's attachments to a chosen directory. |
| `LocalProbe` | Run a Java solution against samples fetched from the problem page. |
| `SilentSubmit` | Submit a source file using the contest's submission form. |
| `LastReport` | Read the latest run's report, falling back to run information when the report is empty. |

## Configuration
The default application setup read `.env` from the process's working directory.
Create it from the provided example before running server-backed actions.
On Windows PowerShell:
```powershell
Copy-Item .env.example .env
```
Or on Linux or macOS:
```sh
cp .env.example .env
```
Replace the credential placeholders and adjust the connection settings:
```dotenv
LOGIN=your_login
PASSWORD=your_password
CONTEST_ID=1
BASE_URL=10.21.17.68
PORT=80
CLIENT_PATH=/ejudge
```

## Solution Markers
Problems are identified by a comment in the source file:
```java
// problem: A
```
Use the problem's short name as shown in the contest. The marker must start at
the beginning of a line; supported names begin with an ASCII letter and contain
only letters and digits.
For submissions, EJavdge first tries the problem page's preset language. When
the page instead offers a language selector, provide a positive, one-based
index into its language choices:
```java
// problem: A
// language: 2
```

## Java Example
There is no command-line `main` entry point in this repository. Call application
actions from your own Java entry point or IDE run configuration, with the
project and its Maven dependencies on the classpath.
For example, the following entry point lists contest problems, reads the
statement for `Main.java`, and runs that solution against the problem's samples:
```java
import java.io.File;
import org.ejavdge.app.AvailableProblems;
import org.ejavdge.app.LocalProbe;
import org.ejavdge.app.ProblemDescription;
import org.ejavdge.file.JavaProgram;
import org.ejavdge.file.JdkFile;
import org.ejavdge.scalar.text.Text;

public final class EjavdgeExample {
    public static void main(String[] args) {
        var solution = new JdkFile(new File("Main.java"));

        new AvailableProblems().run();
        new ProblemDescription(solution).run();
        new LocalProbe(
            new JavaProgram(solution, new Text.Of("."))
        ).run();
    }
}
```
Run this after configured `.env`. `Main.java` must contain a problem marker and an executable Java
`main` method.


## IntelliJ IDEA
See [USAGE.md](USAGE.md) for configuring an External Tool and toolbar action that
passes the active editor file to a scenario runner.

## Project Structure
```text
src/main/java/org/ejavdge/
  app/          Application actions and default configuration
  auth/         Login responses and contest sessions
  contest/      Contest pages, resources, and submission forms
  domain/       Problems, solutions, runs, reports, and session tokens
  dom/          HTML/XML parsing and document selection
  effect/       Side-effect composition
  file/         File access, downloads, and Java program execution
  items/        Collection transformations
  scalar/       Text, byte, and numeric values
  web/          HTTP requests, contexts, media, and socket transport
  workspace/    Environment variables and output handling
src/main/resources/       Logging configuration
src/test/java/            Unit and integration tests
src/test/resources/pages/ Sample ejudge HTML pages
.github/workflows/        Maven verification workflow
```

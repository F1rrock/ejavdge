# EJavdge
EJavdge is a Java client library for working with ejudge contests. It provides
application actions for browsing problems, running Java solutions against sample
tests, submitting source files, and retrieving judging reports.

[![Release](https://jitpack.io/v/F1rrock/ejavdge.svg)](https://jitpack.io/#F1rrock/ejavdge)

## Requirements
- JDK 16 or newer.
- Maven 3.6.3 or newer, or use the included Maven Wrapper (`./mvnw`).
- A compatible ejudge contest and account for actions that access the server.
- The `java` executable on `PATH` for local solution execution.

## Features
| Action                      | Purpose                                                                                                 |
|-----------------------------|---------------------------------------------------------------------------------------------------------|
| `AvailableProblemsApp`      | Show available problem names for the current contest.                                                   |
| `SolvedProblemsApp`         | Show already solved problem names for the current contest.                                              |
| `ProblemDescriptionApp`     | Show description of current problem.                                                                    |
| `LocalTestFilesDownloadApp` | Download the current problem's Local Test files.                                                        |
| `LocalProbeApp`             | Perform local testing of solution.                                                                      |
| `LastReportApp`             | Read the latest run's report, falling back to run information when the report is unavailable.           |
| `SilentSubmitApp`           | Submit current file as solution.                                                                        |
| `SubmitWithNotificationApp` | Submit current file as solution and send a notification when report is available.                       |
| `ReportedSubmitApp`         | Submit current file as solution and display the resulting report.                                       |
| `ProbedSubmitApp`           | Probe current file against local examples, then submit it as solution and display the resulting report. |

## Configuration
The default application setup reads `.env` from the process's working directory.
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
CLIENT_PATH=/new-client
```

## Solution Markers
Problems are identified by a comment in the source file:
```java
// problem: A
```
Use the problem's short name as shown in the contest. The marker must start at
the beginning of a line.
For submissions, EJavdge first tries the problem page's preset language. When
the page instead offers a language selector, provide a positive, one-based
index into its language choices:
```java
// problem: A
// language: 2
```

## Local Testing with Problem Templates
`LocalProbeApp` and the local check in `ProbedSubmitApp` use optional Java
fragments stored as ejudge problem attachments. For `// problem: A`, the
probe prepends `HeaderOfA.java` and appends `FooterOfA.java` when they are
available. Either fragment can be absent. The fragments and the solution are
joined as provided, so include any needed line breaks in the files.

The probe uses the same assembled source for all samples and runs it in the
solution's configured working directory. It leaves the original file unchanged;
`ProbedSubmitApp` submits that original file. Local Test data files linked from
the statement are downloaded separately with `LocalTestFilesDownloadApp`.

## Java Example
There is no command-line `main` entry point in this repository. Call application
actions from your own Java entry point or IDE run configuration, with the
project and its Maven dependencies on the classpath.
For example, the following entry point lists contest problems, reads the
statement for `Main.java`, downloads its Local Test files, and runs the solution
against the problem's samples. Local Test files are linked in the statement
with `name="localtest"` under the university convention. A link can point to
ejudge or another server. The solution reads the downloaded files from its
working directory:

```java
import org.ejavdge.app.LocalTestFilesDownloadApp;
import org.ejavdge.app.AvailableProblemsApp;
import org.ejavdge.app.LocalProbeApp;
import org.ejavdge.app.ProblemDescriptionApp;
import org.ejavdge.file.JavaProgram;
import org.ejavdge.file.JdkFile;
import org.ejavdge.scalar.text.Text;

import java.io.File;

public final class EjavdgeExample {
    public static void main(String[] args) {
        final var solution = new JdkFile(new File("Main.java"));
        final var wd = new Text.Of(".");
        new AvailableProblemsApp().run();
        new ProblemDescriptionApp(solution).run();
        new LocalTestFilesDownloadApp(solution, wd).run();
        new LocalProbeApp(
            new JavaProgram(solution, wd)
        ).run();
    }
}
```
Run this after configuring `.env`. `Main.java` must contain a problem marker.
The source that runs must have a Java `main` method, which can come from the
problem's template.


## IntelliJ IDEA
See [USAGE.md](USAGE.md) for configuring an External Tool and toolbar action that
passes the active editor file to a scenario runner.

## Project Structure
```text
src/main/java/org/ejavdge/
  app/          Application actions, user scenarios and default configuration
  auth/         Login responses and contest sessions
  contest/      Contest pages, resources, and submission forms
  domain/       Problems, solutions, runs, reports, and session tokens
  dom/          HTML/XML parsing and document selection
  effect/       Side-effects and their composition
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

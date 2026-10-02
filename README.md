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
| Action                      | Purpose                                                                                                 |
|-----------------------------|---------------------------------------------------------------------------------------------------------|
| `AvailableProblemsApp`      | Show available problem names for the current contest.                                                   |
| `SolvedProblemsApp`         | Show already solved problem names for the current contest.                                              |
| `ProblemDescriptionApp`     | Show description of current problem.                                                                    |
| `AttachmentsDownloadApp`    | Download attachments of current problem.                                                                |
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
statement for `Main.java`, downloads problem attachments and runs that solution against the problem's samples:

```java
import java.io.File;

import org.ejavdge.app.AttachmentsDownloadApp;
import org.ejavdge.app.AvailableProblemsApp;
import org.ejavdge.app.LocalProbeApp;
import org.ejavdge.app.ProblemDescriptionApp;
import org.ejavdge.file.JavaProgram;
import org.ejavdge.file.JdkFile;
import org.ejavdge.scalar.text.Text;

import java.io.File;

// problem: A1

public final class EjavdgeExample {
    public static void main(String[] args) {
        final var solution = new JdkFile(new File("Main.java"));
        final var wd = new Text.Of(".");
        new AvailableProblemsApp().run();
        new ProblemDescriptionApp(solution).run();
        new AttachmentsDownloadApp(solution, wd).run();
        new LocalProbeApp(
            new JavaProgram(solution, wd)
        ).run();
    }
}
```
Run this after configuring `.env`. `Main.java` must contain a problem marker and an executable Java
`main` method.


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

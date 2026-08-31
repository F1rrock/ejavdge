# EJavdge in IntelliJ IDEA
This guide documents the IntelliJ IDEA equivalent of the ReJudge RStudio **Addins** workflow: open a solution file, click one action, and pass that file to Java code. 

## Short answer
Yes. Configure the Java entry point as an **External Tool**, pass the current editor file with the built-in `$FilePath$` macro, and add the resulting External Tool action to the main toolbar. The user can then open a solution, make the editor active, and click one toolbar button.
External Tools run a command-line executable. For the configured scenario, the executable is Java 26.0.2 and the packaged entry point is `e-scenarios.jar`. The jar receives the subcommand `local-test` followed by the current file path. 

## !!!Required before running: 
`e-scenarios.jar` is not included in this repository and is not produced by `mvn compile`. If IDEA reports `Error: Unable to access jarfile e-scenarios.jar`, copy the supplied jar to the project root (`$ProjectFileDir$\e-scenarios.jar`) or replace the jar argument with its real absolute path, for example `-jar "C:\tools\e-scenarios.jar" local-test "$FilePath$"`. Verify that the file exists before clicking the toolbar button. The `local-test` scenario expects a Java solution/test source file; opening `pom.xml` only demonstrates that `$FilePath$` passes the currently active file and is not itself a JUnit source.


## Prerequisites
1. Open the folder (or project).
2. Wait for Maven import to finish. The project compiler release is Java 16; use JDK 16 or newer in **File | Project Structure...**.
3. Compile the action class first: **Maven / Maven** tool window → **Lifecycle** → `compile`, or **Build | Build Project**.


## 1. Java scenario contract
The packaged Java entry point must accept a subcommand and then the file path. The configuration in this guide uses `local-test <file>`; a future submit action can use another subcommand without changing how the toolbar supplies the file.


## 2. Configure an External Tool
1. Open **File | Settings...** (`Ctrl+Alt+S`), then choose **Tools | External Tools**.
2. Click **Add** (`+`). In **Create Tool**, set **Name** to the exact value `EJavdge: Local testing` , **Group** to `EJavdge`, and **Description** to `Run the currently open JUnit test source file`.
3. Set **Program** to `C:\Program Files\Java\jdk-26.0.2\bin\java.exe` (or your local java.exe directory).
4. Set **Arguments** to: `-jar "e-scenarios.jar" local-test "$FilePath$"`
5. Set **Working directory** to `$ProjectFileDir$`.
6. Under **Advanced Options**, leave **Synchronize files after execution** unchecked, enable **Open console for tool output**, and enable both **Make console active on message in stdout** and **Make console active on message in stderr**.
7. Click **OK** in the tool dialog, then **OK** or **Apply** in Settings.


## 3. Add the action to the main toolbar
1. Ensure the toolbar is visible: **View | Appearance | Toolbar**.
2. Right-click an empty area of the main toolbar and choose **Customize Toolbar...**. If the action appears in the short list, **Add Action to Main Toolbar** can be used.
3. In **Customize Main Toolbar**, select the toolbar group `EJavdge` and click **Add**.
4. Search for the exact action name `EJavdge: Local testing`, select it under **External Tools**, and click **OK**.
5. Optionally select it and use **Edit Icon...** to choose a PNG or SVG icon, then click **OK**.


## 4. Run an action on the current file
1. Open a solution file in the editor and save it (`Ctrl+S`).
2. Keep the editor active, rather than the Project or Maven tool window, so `$FilePath$` refers to the file being edited.
3. Click `EJavdge: Local testing` on the main toolbar.
4. Read the result in the **Run** tool window or the External Tool console. The Java action receives one argument: the open solution's absolute path.
5. Open another solution file and repeat; no tool reconfiguration is needed.


### Example: local testing
Use the `local-test` subcommand from `e-scenarios.jar`. It receives the current solution file as the next argument, runs the local JUnit checker, and prints `PASS` or diagnostic output.
```text
-jar "e-scenarios.jar" local-test "$FilePath$"
```
This is the IDEA analogue of ReJudge's RStudio **Local testing** addin: the user never types a path manually; the active file supplies it. A **Submit the solution** analogue uses the same button pattern with a different jar subcommand, if the scenario jar provides one.


## Reference: JUnit and Maven
JUnit and Maven configurations remain useful for project tests, but they do not replace the current‑file action above.

| Mechanism | Actual entry point | Use |
|---|---|---|
| External Tool | **Settings** → **Tools \| External Tools**; then **Customize Toolbar...** | Run Java code against the active file |
| JUnit run configuration | Green gutter icon → **Run 'PayloadTest'** | Run a class or one test method |
| Maven run configuration | **Maven** tool window → **Lifecycle** → `test` → **Run 'EJavdge [test]'** | Run the project test lifecycle |
| Before Launch | Run configuration → **Before Launch** → **Run External Tool** | Compose preparation with another configuration |
| Macros | **Edit \| Macros \| Start Macro Recording** | Editor repetitions only; macros cannot record toolbar clicks, menus, tool windows, or dialogs |

## Troubleshooting
- **The button is missing.** Reopen **Customize Toolbar...** and search under **External Tools**. Confirm the tool is checked in **Settings → Tools → External Tools**.
- **`$FilePath$` is wrong.** Put focus in the editor, save the file, and click the button again. Do not launch it while a Project or Maven tool window is focused.
- **Java cannot find the class.** Run `compile`, confirm the `.class` file exists under `target/classes`, and check the `-cp` path and JDK in **Program**.
- **The editor is stale after the action edits a file.** Enable **Synchronize files after execution**.

## Sources

- [IntelliJ IDEA: External tools](https://www.jetbrains.com/help/idea/configuring-third-party-tools.html)
- [IntelliJ IDEA: External tools settings](https://www.jetbrains.com/help/idea/settings-tools-external-tools.html)
- [IntelliJ IDEA: Menus and toolbars](https://www.jetbrains.com/help/idea/customize-actions-menus-and-toolbars.html)
- [IntelliJ IDEA: Maven tool window](https://www.jetbrains.com/help/idea/maven-projects-tool-window.html)
- [IntelliJ IDEA: Run/debug configurations](https://www.jetbrains.com/help/idea/run-debug-configurations-dialog.html)
- [ReJudge usage guide](https://github.com/F1rrock/re-judge/blob/main/USAGE.md)

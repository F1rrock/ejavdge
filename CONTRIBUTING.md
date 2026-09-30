# Contributing

## Build and test

Requires JDK 16+ and Maven.

```sh
mvn verify
```

The build runs Checkstyle, PMD, SpotBugs, unit tests, and
integration tests. Integration tests use sample eJudge HTML pages
from `src/test/resources/pages` and do not require a live server.

## Style

The project follows the conventions of Elegant Objects:

- constructors build objects, never perform I/O;
- no setters, no getters without a reason;
- no `static` mutable state;
- no null returns — throw `InvariantViolation` instead;
- prefer composition over inheritance;
- one class — one role.

Read the code in `domain/` and `effect/` before contributing: it is
the best reference for the style.

## Commits

[Conventional Commits](https://www.conventionalcommits.org/):

```
feat: add X
fix: correct Y
refactor: extract Z
docs: update README
chore: bump dependency
```

Keep the subject under 72 characters. Use the body for *why*, not
*what*.

## Pull requests

- one logical change per PR;
- tests for new behavior;
- `mvn verify` passes locally;
- link related issues in the description.

# Security Policy

## Supported Versions

Only the latest release is supported with security updates.

| Version | Supported   |
|---------|-------------|
| latest  | ✅           |
| older   | ❌           |

## Reporting a Vulnerability

If you discover a security issue in EJavdge, please report it
privately rather than opening a public issue.

Use GitHub's [private vulnerability reporting][gh-report] on this
repository, or email the maintainer directly.

Please include:

- a description of the issue;
- steps to reproduce;
- the affected version and environment;
- any suggested fix, if you have one.

You will receive a response within a few days. If the issue is
confirmed, a fix will be prepared and released as soon as possible,
and you will be credited in the release notes unless you prefer
to remain anonymous.

[gh-report]: https://docs.github.com/en/code-security/security-advisories/guidance-on-reporting-and-writing-information-about-vulnerabilities/privately-reporting-a-security-vulnerability

## Scope

EJavdge handles eJudge credentials and performs HTTP requests on
behalf of the user. The following are considered in scope:

- credential leakage through logs, exceptions, or `.env` handling;
- unsafe HTTP request construction (header injection, URL confusion);
- path traversal in attachment downloads or report fetching.

Issues in eJudge itself, or in third-party dependencies, are out of
scope for this repository — please report those upstream.

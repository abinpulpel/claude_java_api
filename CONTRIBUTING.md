# Contributing to claude_java_api

Thank you for considering a contribution to this framework. This document
covers everything you need to get set up and submit a good pull request.

## Prerequisites

- Java 21 (LTS) or later
- Maven 3.9+
- Docker (optional, for containerized execution)

## Getting Started

```bash
git clone https://github.com/abinpulpel/claude_java_api.git
cd claude_java_api
mvn clean install -DskipTests
```

## Running Tests

```bash
# Full suite, default environment (qa)
mvn test

# Specific environment
mvn test -Denv=staging

# Specific TestNG suite
mvn test -DsuiteXmlFile=src/test/resources/testng.xml
```

## Project Structure

See [README.md](README.md#folder-structure) for the full folder layout and
architectural overview before making changes.

## Branching and Commits

- Branch from `main`: `feature/<short-description>`, `fix/<short-description>`.
- Use clear, present-tense commit messages (e.g., `Add retry analyzer for flaky tests`).
- Keep commits scoped to a single logical change.

## Code Standards

- Follow the existing package structure: `config`, `specs`, `clients`,
  `endpoints`, `models`, `listeners`, `utils`.
- New endpoints belong in `endpoints/`, wrapping a `BaseApiClient` call —
  do not call Rest Assured directly from test classes.
- New request/response models belong in `models/` with Jackson annotations.
- Add Javadoc to all public classes and methods.
- Run `mvn checkstyle:check` before submitting a PR.
- No hard-coded environment values — use `ConfigManager`.

## Pull Request Checklist

- [ ] Code compiles: `mvn clean compile`
- [ ] Tests pass locally: `mvn test`
- [ ] Checkstyle passes: `mvn checkstyle:check`
- [ ] New endpoints/models include Javadoc
- [ ] README updated if behavior or setup steps changed
- [ ] CHANGELOG updated under `[Unreleased]`

## Reporting Issues

Please include: the command you ran, the environment (`qa`/`staging`/`prod`),
expected vs. actual behavior, and relevant log/Allure output.

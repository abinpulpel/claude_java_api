# claude_java_api

Reusable, enterprise-grade **Java + Rest Assured** API automation framework.
Designed so QA engineers can immediately start writing automated API tests by
importing existing configuration, request/response specs, endpoint clients,
and data-driven utilities — no boilerplate required.

## Overview

This framework provides a production-ready foundation for REST API test
automation:

- Layered YAML configuration (base + per-environment overrides), with system
  property and environment-variable overrides on top.
- `RequestSpecFactory` / `ResponseSpecFactory` centralizing Rest Assured spec
  construction — no ad-hoc `given()` calls scattered across tests.
- A `BaseApiClient` wrapping Rest Assured's `given()`, composed by
  resource-oriented endpoint classes (Repository pattern).
- Jackson-backed POJO models with a fluent Builder (`Post`) and nested value
  objects (`User.Address`, `User.Geo`, `User.Company`).
- JSON schema validation of response contracts via `json-schema-validator`.
- TestNG execution with a custom `RetryAnalyzer` (auto-attached via
  `AnnotationTransformer`), parallel execution, and a `TestListener` for
  lifecycle logging.
- `DataReader` utility supporting JSON, CSV, and Excel data-driven test data.
- Allure reporting integration.
- SLF4J + Logback logging.
- Docker support for containerized execution.
- GitHub Actions CI with a QA/staging execution matrix, JaCoCo coverage, and
  Checkstyle static analysis.
- Sample test suites (`PostsApiTest`, `UsersApiTest`) against the public,
  no-auth [JSONPlaceholder](https://jsonplaceholder.typicode.com) API.

## Architecture

```
Test Layer (src/test/java/.../tests)
   -> Endpoint Clients (src/main/java/.../endpoints)     [Repository pattern]
        -> BaseApiClient (src/main/java/.../clients)
             -> RequestSpecFactory / ResponseSpecFactory  [Factory pattern]
                  -> Rest Assured
   -> Models (src/main/java/.../models)                   [Builder pattern]
   -> ConfigManager (layered YAML + env var resolution)   [Singleton]
   -> DataReader (JSON / CSV / Excel fixtures)
```

Design principles: Repository pattern (endpoint clients), Factory pattern
(request/response specs), Builder pattern (`Post`), Singleton (config
manager), Dependency composition over inheritance (`BaseApiClient` composed
by endpoint classes), single-responsibility utility classes.

## Folder Structure

```
claude_java_api/
├── src/main/java/com/claude/api/
│   ├── config/ConfigManager.java        # Layered configuration singleton
│   ├── enums/Environment.java           # Supported environment enum
│   ├── exceptions/                      # Framework exception hierarchy
│   ├── specs/                           # Request/response spec factories
│   ├── clients/BaseApiClient.java       # Rest Assured given() wrapper
│   ├── endpoints/                       # PostsEndpoints, UsersEndpoints
│   ├── models/                          # Post, User (+ nested value objects)
│   ├── listeners/                       # RetryAnalyzer, AnnotationTransformer, TestListener
│   └── utils/                           # JsonUtils, DataReader
├── src/main/resources/
│   ├── config/                          # config.yaml + per-environment overrides
│   └── logback.xml
├── src/test/java/com/claude/api/tests/
│   ├── BaseTest.java
│   ├── PostsApiTest.java
│   └── UsersApiTest.java
├── src/test/resources/
│   ├── schemas/                         # post-schema.json, user-schema.json
│   ├── testdata/                        # posts_data.json, users_data.csv
│   └── testng.xml
├── .github/workflows/ci.yml             # CI: QA/staging matrix
├── Dockerfile / docker-compose.yml
└── pom.xml
```

## Technology Stack

| Concern             | Choice                              |
|----------------------|--------------------------------------|
| Language             | Java 21 (LTS)                       |
| Build tool           | Maven                               |
| API client           | Rest Assured 5.5+                   |
| Test runner          | TestNG 7.x                          |
| Schema validation    | rest-assured json-schema-validator  |
| JSON / YAML          | Jackson, SnakeYAML                  |
| Data-driven          | JSON, CSV (Commons CSV), Excel (POI)|
| Reporting            | Allure                              |
| Logging              | SLF4J + Logback                     |
| Code coverage        | JaCoCo                              |
| Static analysis      | Checkstyle                          |
| Containerization     | Docker                              |
| CI/CD                | GitHub Actions                      |

## Installation

### Prerequisites

- Java 21 or later
- Maven 3.9+
- Docker (optional, for containerized execution)

### Setup

```bash
git clone https://github.com/abinpulpel/claude_java_api.git
cd claude_java_api
mvn clean install -DskipTests
```

## Configuration

Configuration is layered: `config/config.yaml` (on the classpath under
`src/main/resources`) provides defaults, and `config/config-<env>.yaml`
overrides them per environment (`qa`, `staging`, `prod`). Any key can also be
overridden via a JVM system property (`-Dkey=value`) or an environment
variable of the same name (uppercased). Precedence, highest first: system
property > environment variable > `config-<env>.yaml` > base `config.yaml`.

## Execution

```bash
# Full suite, default environment (qa)
mvn test

# Specific environment
mvn test -Denv=staging

# Specific TestNG suite file
mvn test -DsuiteXmlFile=src/test/resources/testng.xml

# Smoke group only
mvn test -Dgroups=smoke
```

### Parallel Execution

`src/test/resources/testng.xml` runs with `parallel="methods"
thread-count="4"` by default; override via `parallel_thread_count` in
configuration or `-Dthread-count` on the command line.

### Docker

```bash
docker compose up --build
```

Builds the project inside a Maven/Temurin 21 image and runs the suite against
the environment specified by the `ENV` build/runtime variable.

### CI/CD

`.github/workflows/ci.yml` runs the suite across a QA/staging matrix on every
push and pull request, running Checkstyle, generating a JaCoCo coverage
report, and uploading Allure results and Surefire reports as build artifacts.

## Reporting

Allure results are written to `allure-results/`. Generate and view the HTML
report locally with:

```bash
allure serve allure-results
```

Standard Surefire reports are also written to `target/surefire-reports/`.

## Logging

SLF4J + Logback write to both the console and a rolling file under `logs/`.
Configure verbosity in `src/main/resources/logback.xml`.

## Troubleshooting

- **`FrameworkException: Required configuration file not found`**: ensure
  `src/main/resources/config/config.yaml` is on the classpath (run via Maven
  from the repository root).
- **Schema validation failures**: confirm the response shape against
  `src/test/resources/schemas/*.json`; JSONPlaceholder occasionally adds
  fields, which `additionalProperties` settings will surface.
- **Retries not applying**: retries are wired via `AnnotationTransformer` in
  `testng.xml` listeners — ensure it is not removed from the suite file.

## Contribution Guide

See [CONTRIBUTING.md](CONTRIBUTING.md) for setup, branching, and PR standards.

## License

Distributed under the [MIT License](LICENSE).

## Roadmap

- OAuth2 / API key authentication helper specs
- Contract testing (Pact) integration
- GraphQL client support
- Cloud execution profiles (SauceLabs API testing, BrowserStack)

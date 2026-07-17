# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.0.0] - 2026-07-17

### Added

- Initial release of `claude_java_api`, a reusable Java + Rest Assured API automation framework.
- Layered YAML configuration (base + per-environment overrides) via `ConfigManager` singleton.
- `RequestSpecFactory` / `ResponseSpecFactory` for centralized request/response spec building.
- `BaseApiClient` wrapping Rest Assured's `given()` with logging and spec injection.
- Repository-pattern endpoint classes (`PostsEndpoints`, `UsersEndpoints`) for resource-oriented API calls.
- POJO request/response models with Jackson (de)serialization and a fluent builder for `Post`.
- JSON schema validation for response contracts.
- TestNG-based execution with `RetryAnalyzer`, `AnnotationTransformer`, and a custom `TestListener`.
- Parallel test execution via `testng.xml` (parallel="methods").
- `DataReader` utility supporting JSON, CSV, and Excel data-driven test data.
- Allure reporting integration.
- SLF4J + Logback logging.
- Dockerfile and docker-compose for containerized execution.
- GitHub Actions CI workflow with JaCoCo coverage and Checkstyle static analysis.
- Sample test suites against the public JSONPlaceholder API (`PostsApiTest`, `UsersApiTest`).

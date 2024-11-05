# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- param tryColumnType, will try to format column by type.

## [1.0.1] - 2024-07-16

### Added

- README Chapter explaining how to use the library as a simple sql query catalog.

### Changed

- fj-version set to 8.6.2
- fj-bom set to 1.6.5

## [1.0.0] - 2024-02-17

### Changed

- fj-version set to 8.4.8
- fj-bom set to 1.6.0
- Upgraded build_maven_package workflow to version 1.0.1, (accespt DISABLE_MAVEN_DEPENDENCY_SUBMISSION)
- [Workflow review and transition to node 20](https://github.com/fugerit-org/fj-universe/issues/29)

## [0.4.2] - 2023-11-24

## [0.4.1] - 2023-09-24

### Added

- queryFile handling in query-catalog.xml configuration file

### Fixed

- main class in pom

### Added

- Code of conduct badge and file
- [Sample jdk compatibility check workflow on branch develop](.github/workflows/build_maven_compatibility.yml)

### Changed

- [Sonar cloud workflow merged in maven build](.github/workflows/deploy_maven_package.yml)
- fj-bom version set to 1.4.0

### Removed

- Sonar cloud workflow yml removed. (after being merged with maven build)

## [0.4.0] - 2023-09-23

### Added

- [workflow deploy on branch deploy](.github/workflows/deploy_maven_package.yml)
- [workflow maven build](.github/workflows/build_maven_package.yml)
- keep a changelog and coverage badge

### Changed

- [workflow sonar cloud](.github/workflows/sonarcloud-maven.yml)
- fj-bom version set to 1.4.0
- fj-core version set to 8.3.8


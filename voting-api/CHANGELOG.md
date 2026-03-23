## [1.3.2] - 2026-03-22
### Added
- Unit tests for AgendaService.
- Unit tests for SessionService.
- Unit tests for VoteResultService.

### Changed
- Standardized VowServiceTest structure using @DisplayName, Arrange, Act, and Assert.
- Fixed 404 bug in VoteIntegrationTest related to vote register route and missing payload attributes.

## [1.2.2]
### Added
- Adding a route to search for agenda items by ID.

## [1.2.1]
### Added
- API versioning using /api/v1 prefix

## [1.1.1]
### Changed
- Replaced Object[] result query with Spring Data projection for vote results

## [1.1.0]
### Added
- Database indexes for vote performance
- Unique constraint to prevent duplicate votes
- Optimized queries for vote counting

## [1.0.0]
### Added
- Fake external CPF validation service
- External client architecture
- CPF validation before voting

## [0.8.0]
### Added
- Dockerfile for API
- docker-compose with PostgreSQL
- One-command project execution

## [0.7.0]
### Added
- Swagger/OpenAPI documentation
- Endpoint documentation with @Operation and @Tag

## [0.6.0]
### Added
- Integration tests using H2 database
- Full voting flow test:
  - create pauta
  - open session
  - vote
  - get result

## [0.5.0]
### Added
- Unit tests for vote service
- Test coverage for:
    - successful vote
    - duplicate vote
    - closed session validation

## [0.4.0]
### Added
- Voting result endpoint
- Optimized vote counting directly in database
- Result calculation (SIM venceu / NAO venceu / Empate)

## [0.3.0]
### Added
- Vote registration endpoint
- Validation: associate can vote only once per pauta
- Validation: vote only allowed while session is open

## [0.2.0]
### Added
- Voting session creation with default duration rule

## [0.1.0]
### Added
- Create pauta endpoint
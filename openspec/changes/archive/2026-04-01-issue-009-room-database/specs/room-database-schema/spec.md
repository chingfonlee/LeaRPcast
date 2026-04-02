## ADDED Requirements

### Requirement: Room Schema Matches Data Schema v1
All entity table structures MUST match the column definitions in `radio_podcast_data_schema_v_1.md` exactly (names, types, nullability).

#### Scenario: Database creation
- **WHEN** `AppDatabase` is built for the first time
- **THEN** Room creates all tables with columns matching Data Schema v1 and `exportSchema` produces a valid JSON schema file.

### Requirement: AppDatabase Singleton
`AppDatabase` MUST be a Room database class annotated with `@Database`, listing all entity classes and version = 1.

#### Scenario: DI provides database
- **WHEN** Hilt resolves `AppDatabase`
- **THEN** a single `Room.databaseBuilder(...)` instance is returned (singleton).

## ADDED Requirements

### Requirement: Radio Station List from Cache-First Repository
`DefaultRadioRepository.observeStations()` MUST emit from local DB first and trigger a network refresh to keep data current.

#### Scenario: First launch (empty cache)
- **WHEN** `observeStations()` is collected and local DB is empty
- **THEN** the repository fetches from RadioBrowser API, inserts results, and the Flow emits the stations.

#### Scenario: Subsequent launches (cache available)
- **WHEN** `observeStations()` is collected and local DB has data
- **THEN** cached stations emit immediately; background refresh runs silently.

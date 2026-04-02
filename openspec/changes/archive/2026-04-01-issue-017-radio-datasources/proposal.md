## Why

Radio data is served by the RadioBrowser API. We need a remote data source to fetch stations and a local data source to cache them in Room, surfaced through a repository for the domain layer.

## What Changes

- Create `RadioRemoteDataSource` consuming RadioBrowser API via Retrofit.
- Create `RadioLocalDataSource` wrapping the `RadioStationDao`.
- Create `DefaultRadioRepository` orchestrating local cache + network fallback.

## Capabilities

### New Capabilities
- `radio-data-layer`: Remote + local data sources and repository for radio station data.

### Modified Capabilities
-

## Impact

- Modules: `:core:network`, `:core:database`, repository layer
- Unlocks ISSUE-018, ISSUE-019.

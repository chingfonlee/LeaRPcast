## Context

Radio station data comes from RadioBrowser API (JSON over HTTPS). Local caching in Room keeps stations available offline. Repository exposes a `Flow`-based live stream.

## Goals / Non-Goals

**Goals:** Retrofit-based `RadioBrowserApiService`, `RadioRemoteDataSource`, `RadioLocalDataSource`, `DefaultRadioRepository`.
**Non-Goals:** Favourite toggle persistence (future), search (future).

## Decisions

Use Retrofit + `converter-scalars` or JSON (RadioBrowser returns JSON). Query top stations by click count initially.

## Risks / Trade-offs

- **Risk**: RadioBrowser server availability varies (community-maintained).
  - **Mitigation**: Always serve from local cache first; show network error if cache is empty.

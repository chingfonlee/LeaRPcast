## Context

Radio screen with station list and tap-to-play. Uses `RadioViewModel` collecting from `DefaultRadioRepository` and calling `PlayRadioUseCase`.

## Goals / Non-Goals

**Goals:** `RadioScreen`, `RadioViewModel`, `RadioUiState`, station list `LazyColumn`, tap to play, favourite placeholder button.
**Non-Goals:** Search/filter (future), persistent favourites (future).

## Decisions

Show loading spinner while cache is empty and first remote fetch runs. Error state shown when both cache and network fail.

## Risks / Trade-offs

- **Risk**: Empty state on first launch until network resolves.
  - **Mitigation**: Show "Loading stations…" text string.
